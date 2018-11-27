package com.ainijar.controller;

import com.ainijar.common.config.Result;
import com.ainijar.common.constant.WxEventType;
import com.ainijar.common.constant.WxMsgType;
import com.ainijar.common.util.RedisUtil;
import com.ainijar.common.util.XmlUtil;
import com.ainijar.model.*;
import com.ainijar.service.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@Slf4j
@Api(description = "接口文档")
@RequestMapping("/wechat")
public class WechatController {

    @Autowired
    private WechatService wechatService;

    @Autowired
    RedisUtil redisUtil;

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

    /**
     * 处理微信消息的回调
     *
     * @param xml post消息内容
     *
     * @return 消息处理结果
     *
     * @throws IOException xml读写异常
     * @throws DocumentException xml转换异常
     */
    @PostMapping("/wxReceive")
    public String receiveMessage(String signature, String timestamp, String nonce, String echostr, @RequestBody(required = false) String xml, HttpServletRequest request) throws Exception {
        if (!wechatService.signature(signature, timestamp, nonce)) {
            return "failed";
        }
        String openid = request.getParameter("openid");
        System.out.println(openid);
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        //消息发送者/接收者
        String fromUserName = root.elementTextTrim("FromUserName");
        // 公众帐号
        String toUserName = root.elementTextTrim("ToUserName");
        //Create Time
        String createTime = root.elementTextTrim("CreateTime");
        // 消息类型
        String msgType = root.elementTextTrim("MsgType");
        String msgId = root.elementTextTrim("MsgID");

        Map map = XmlUtil.xmlToMap(xml);
        Map map1 = XmlUtil.parseXml(xml);
        XmlUtil.mapToXml(map1);
        //消息key,判断是否在15秒内收到过该消息,收到过直接返回
        String msgKey = Objects.isNull(msgId) ? (fromUserName + "_" + createTime) : msgId;
        if (Objects.nonNull(redisUtil.get(msgKey))) {
            return "success";
        }
        //设置20秒缓存
        redisUtil.setEx(msgKey, createTime, (long) 20);
        switch (WxMsgType.getByName(msgType)) {
            //处理事件消息
            case EVENT:
                String eventType = root.elementTextTrim("Event");
                String eventKey = root.elementTextTrim("EventKey");
                switch (WxEventType.getByName(eventType)) {
                    case TEMPLATE_SEND_JOB_FINISH:
                        //处理模版消息推送事件回调
                        System.out.println("处理模版消息推送事件回调");
                        break;
                    case SCAN:
                        //处理扫码事件
                        System.out.println("处理扫码事件");
                        break;
                    case SUBSCRIBE:
                        //用户关注事件
                        System.out.println("用户关注事件");
                        break;
                    case UNSUBSCRIBE:
                        //用户取消关注事件
                        System.out.println("用户取消关注事件");
                        break;
                }
                break;
            case TEXT:
                //处理文本消息
                root.elementTextTrim("Content");
                break;
            default:
                //默认处理其余消息
                break;
        }
        return "success";
    }

    /**
     * 验证接口配置信息
     * 加密/校验流程如下：
     * 将token、timestamp、nonce三个参数进行字典序排序
     * 将三个参数字符串拼接成一个字符串进行sha1加密
     * 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     */
    @GetMapping("/wxReceive")
    public String signature(String signature, String timestamp, String nonce, String echostr) throws IOException {
        if (wechatService.signature(signature, timestamp, nonce) && echostr != null) {
            return echostr;
        }
        return "failed";
    }

}
