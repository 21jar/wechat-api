package com.ainijar.service;

import com.ainijar.dto.*;

public interface IWechatService {
    AccessToken accessToken();

    QrCodeResult createQrCode(QrCode qrCode);

    String downloadQrCode(String ticket);

    boolean signature(String signature, String timestamp, String nonce);

    BaseResult sendTemplateMsg(TemplateMessage templateMessage);

    WechatUserInfo getUserInfo(String openid);

    void createMenu(String fileName);
}
