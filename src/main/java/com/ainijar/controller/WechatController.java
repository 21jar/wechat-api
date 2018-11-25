package com.ainijar.controller;

import com.ainijar.common.config.Result;
import com.ainijar.model.AccessToken;
import com.ainijar.model.QrCode;
import com.ainijar.model.QrCodeResult;
import com.ainijar.service.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(description = "接口文档")
@RequestMapping("/wechat")
public class WechatController {

    @Autowired
    private WechatService wechatService;

    @GetMapping("/accessToken")
    @ApiOperation(value = "接口", notes = "接口")
    public Result accessToken() {
        AccessToken accessToken = wechatService.accessToken();
        return Result.success(accessToken, "成功");
    }

    @GetMapping("/createQrCode")
    @ApiOperation(value = "接口", notes = "接口")
    public Result createQrCode() {
        QrCode qrCode = new QrCode();
        qrCode.setActionName(QrCode.QrCodeType.QR_STR_SCENE);
        qrCode.setExpireSeconds(2592000);
        QrCode.Scene scene = new QrCode.Scene();
        scene.setSceneStr("111");
        qrCode.setActionInfo(new QrCode.ActionInfo(scene));
        QrCodeResult qrCodeResult = wechatService.createQrCode(qrCode);
        return Result.success(qrCodeResult, "成功");
    }

    @GetMapping("/downloadQrCode")
    @ApiOperation(value = "接口", notes = "接口")
    public Result downloadQrCode() {
        QrCode qrCode = new QrCode();
        qrCode.setActionName(QrCode.QrCodeType.QR_STR_SCENE);
        qrCode.setExpireSeconds(2592000);
        QrCode.Scene scene = new QrCode.Scene();
        scene.setSceneStr("111");
        qrCode.setActionInfo(new QrCode.ActionInfo(scene));
        QrCodeResult qrCodeResult = wechatService.createQrCode(qrCode);

        String url = wechatService.downloadQrCode(qrCodeResult.getTicket());
        return Result.success(url, "成功");
    }


}
