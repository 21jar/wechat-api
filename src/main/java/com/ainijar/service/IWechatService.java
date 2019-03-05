package com.ainijar.service;

import com.ainijar.dto.*;

import java.util.Map;

public interface IWechatService {

    AccessToken accessToken(String appId, String appSecret);

    QrCodeResult createQrCode(QrCode qrCode, String appId, String appSecret);

    String downloadQrCode(String ticket);

    boolean signature(String signature, String timestamp, String nonce);

    BaseResult sendTemplateMsg(TemplateMessage templateMessage, String appId, String appSecret);

    void createMenu(String fileName, String appId, String appSecret);

    WechatUserInfo getUserInfo(String appId, String appSecret, String openid);

    Map oauth2GetAccesstoken(String appId, String appSecret, String code);
}
