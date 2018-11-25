package com.ainijar.service.impl;

import com.ainijar.common.constant.WechatConst;
import com.ainijar.common.util.RedisUtil;
import com.ainijar.model.AccessToken;
import com.ainijar.model.QrCode;
import com.ainijar.model.QrCodeResult;
import com.ainijar.model.ResultCheck;
import com.ainijar.service.WechatService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Objects;

@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Value("${appId}")
    private String appId;

    @Value("${appSecret}")
    private String appSecret;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private RestTemplate restTemplate;

    synchronized private AccessToken refreshAccessToken() {
        try {
            Object accessTokenObj = redisUtil.get(appId);
            if (Objects.isNull(accessTokenObj)) {
                String url = WechatConst.URL_GET_ACCESSTOEKN
                        .replace("APPID", appId)
                        .replace("APPSECRET",appSecret);
                AccessToken accessToken = restTemplate.getForObject(url, AccessToken.class);
                if (ResultCheck.isSuccess(accessToken)) {
                    accessToken.setLastRefreshTime(System.currentTimeMillis());
                    redisUtil.setEx(appId, JSON.toJSONString(accessToken), AccessToken.EXPIRE_TIME);
                } else {
                    throw new Exception(accessToken.toString());
                }
                return accessToken;
            } else {
                return JSON.parseObject(String.valueOf(accessTokenObj), AccessToken.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取accessToken失败：" + e.getMessage());
            return null;
        }
    }

    @Override
    public AccessToken accessToken() {
        Object accessTokenObj = redisUtil.get(appId);
        if (Objects.nonNull(accessTokenObj)) {
            return JSON.parseObject(String.valueOf(accessTokenObj), AccessToken.class);
        }
        return refreshAccessToken();
    }

    @Override
    public QrCodeResult createQrCode(QrCode qrCode) {
        String url = WechatConst.URL_GET_QR_CODE.replace("TOKEN", accessToken().getAccessToken());
        return restTemplate.postForObject(MessageFormat.format(url, accessToken().getAccessToken()), qrCode, QrCodeResult.class);
    }

    @Override
    public String downloadQrCode(String ticket) {
        String url = null;
        try {
            url = WechatConst.URL_DOWNLOAD_QR_CODE.replace("TICKET", URLEncoder.encode(ticket,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        return restTemplate.postForEntity(url,null, ByteArrayResource.class);
        return url;
    }
}
