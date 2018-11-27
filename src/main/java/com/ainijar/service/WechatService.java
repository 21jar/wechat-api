package com.ainijar.service;

import com.ainijar.model.AccessToken;
import com.ainijar.model.QrCode;
import com.ainijar.model.QrCodeResult;

public interface WechatService {
    AccessToken accessToken();

    QrCodeResult createQrCode(QrCode qrCode);

    String downloadQrCode(String ticket);

    boolean signature(String signature, String timestamp, String nonce);

    String sendTemplateMsg();
}
