package com.ainijar.service.impl;

import com.ainijar.common.constant.WechatConst;
import com.ainijar.common.util.PropertiesUtil;
import com.ainijar.common.util.RestUtil;
import com.ainijar.model.*;
import com.ainijar.service.WechatService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Value("${appId}")
    private String appId;

    @Value("${appSecret}")
    private String appSecret;

//    @Autowired
//    RedisUtil redisUtil;

    @Autowired
    private RestUtil restUtil;

    @Autowired
    private RestTemplate restTemplate;

    synchronized private AccessToken refreshAccessToken() {
        try {
            Object accessTokenObj = null;
//            Object accessTokenObj = redisUtil.get(appId);
            if (Objects.isNull(accessTokenObj)) {
                String url = WechatConst.URL_GET_ACCESSTOEKN
                        .replace("APPID", appId)
                        .replace("APPSECRET", appSecret);
                AccessToken accessToken = restTemplate.getForObject(url, AccessToken.class);
                if (ResultCheck.isSuccess(accessToken)) {
                    accessToken.setLastRefreshTime(System.currentTimeMillis());
//                    redisUtil.setEx(appId, JSON.toJSONString(accessToken), AccessToken.EXPIRE_TIME);
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
        Object accessTokenObj = null;
//        Object accessTokenObj = redisUtil.get(appId);
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
            url = WechatConst.URL_DOWNLOAD_QR_CODE.replace("TICKET", URLEncoder.encode(ticket, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 验证公众号签名有效性
     *
     * @param signature 原加密标志
     * @param timestamp 加密时间戳
     * @param nonce     加密随机字符串
     * @return 是否是微信消息
     */
    @Override
    public boolean signature(String signature, String timestamp, String nonce) {
        if (signature == null ||timestamp == null || nonce == null){
            return false;
        }
        List<String> list = Arrays.asList(timestamp, "token", nonce);
        //1.字典排序
        Collections.sort(list);
        //2.sha1加密
        String str = DigestUtils.sha1Hex(list.get(0) + list.get(1) + list.get(2));
        //3.判断是否微信信息
        return signature.equals(str);
    }

    /**
     * 发送模板消息
     * @return
     */
    @Override
    public boolean sendTemplateMsg(WeixinTemplateMsg weixinTemplateMsg){
        String url = WechatConst.URL_TEMPLATE_SEND.replace("ACCESS_TOKEN", accessToken().getAccessToken());
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<>(JSON.toJSONString(weixinTemplateMsg), headers);
        BaseResult rep = restTemplate.postForObject(url, formEntity, BaseResult.class);
        return ResultCheck.isSuccess(rep);
    }

    /**
     * 创建菜单
     */
    @Override
    public void createMenu(String fileName) {
        // 拼装创建菜单的url
        String url = WechatConst.URL_CREATE_MENU.replace("ACCESS_TOKEN", accessToken().getAccessToken());
        // 将菜单对象转换成json字符串
        String jsonMenu = PropertiesUtil.readJsonData(fileName);
        if (jsonMenu != null) {
            // 调用接口创建菜单
            JSONObject jsonObject = null;
            try {
                jsonObject = restUtil.restPostByBody(jsonMenu.getBytes("UTF-8"), url, JSONObject.class);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (null != jsonObject) {
                if (0 != jsonObject.getInteger("errcode")) {
                    log.error("create menu fail errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
                } else {
                    log.info("create menu success errcode:{} errmsg:{}",jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
                }
            }
        }
    }

}
