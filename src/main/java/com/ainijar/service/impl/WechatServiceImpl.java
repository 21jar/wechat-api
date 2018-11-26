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
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
                        .replace("APPSECRET", appSecret);
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
            url = WechatConst.URL_DOWNLOAD_QR_CODE.replace("TICKET", URLEncoder.encode(ticket, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        return restTemplate.postForEntity(url,null, ByteArrayResource.class);
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


//    public String wxMessage(HttpServletRequest request) {
//
//        Map<String, String> map = wxUtils.receiveMessage(request);
////		System.out.println(map.toString());
//        if (map == null || map.size() == 0) {
//            return null;
//        }
//
//        // 微信消息类型判断
//        String msgType = map.get("MsgType");
//        String reply = null;
//
//        switch (msgType) {
//            case Configuration.MESSAGE_TEXT:
//                reply = wxUtils.replyTextMessage(map, "你好");
//                // System.out.println(reply);
//
////			if (map.get("Content").equals("1")) {
////				Article article = new Article();
////				article.setTitle("测试公众号");
////				article.setDescription("测试描述");
////				article.setPicUrl("http://img3.imgtn.bdimg.com/it/u=2487184179,3100424350&fm=23&gp=0.jpg");
////				article.setUrl("http://www.baidu.com");
////
////				List<Article> articles = new ArrayList<>();
////				articles.add(article);
////
////				Article article1 = new Article();
////				article1.setTitle("测试公众号2");
////				article1.setDescription("测试描述2");
////				article1.setPicUrl("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1353085406,3342692239&fm=23&gp=0.jpg");
////				article1.setUrl("http://www.baidu.com");
////
////				articles.add(article1);
////				reply = wxUtils.replyNewsMessage(map, articles);
////				// System.out.println(reply);
////			} else if (map.get("Content").equals("2")) {
////				Article article = new Article();
////				article.setTitle("测试公众号2");
////				article.setDescription("测试描述2");
////				article.setPicUrl("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1353085406,3342692239&fm=23&gp=0.jpg");
////				article.setUrl("http://www.baidu.com");
////
////				List<Article> articles = new ArrayList<>();
////				articles.add(article);
////
////				reply = wxUtils.replyNewsMessage(map, articles);
////			}
//
//                break;
//            case Configuration.MESSAGE_EVENT:
//                String event = map.get("Event");
//                if (Configuration.MESSAGE_SUBSCRIBE.equals(event)) {
//                    // 订阅消息
//                    reply = wxUtils.replyTextMessage(map, "你好，欢迎订阅房公信");
//                } else if (Configuration.MESSAGE_UNSUBSCRIBE.equals(event)) {
//                    // 取消订阅消息
//                    reply = wxUtils.replyTextMessage(map, "。。。");
//                }
//                break;
//            case Configuration.MESSAGE_IMAGE:
//
//                break;
//            case Configuration.MESSAGE_LINK:
//
//                break;
//
//            default:
//                break;
//        }
//
//        return reply;
//
//    }

}
