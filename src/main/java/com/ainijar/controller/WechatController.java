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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

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

    /**
     * 处理微信消息的回调
     *
     * @param validParams 微信验证参数
     * @param xml post消息内容
     *
     * @return 消息处理结果
     *
     * @throws IOException xml读写异常
     * @throws AesException 解密异常
     * @throws DocumentException xml转换异常
     */
//    @PostMapping("wxReceive.do")
//    public String receiveMessage(WxMsgValidParams validParams, @RequestBody(required = false) String xml) throws IOException, AesException, DocumentException {
//        log.info(validParams.toString());
//
//        log.info(xml);
//        if (!wxApiService.signature(validParams)) {
//            return "failed";
//        } else {
//            String replyMsg = "success";
//            //判断该消息是否为加密消息
//            if (validParams.isEncrypt()) {
//                xml = (new WXBizMsgCrypt(wxProperties.getToken(), wxProperties.getEncodingAesKey(), wxProperties.getAppId()))
//                        .DecryptMsg(validParams.getMsg_signature(), validParams.getTimestamp(), validParams.getNonce(), xml);
//            }
//            log.info("\n收到微信消息:\n" + xml);
//
//            Document document = DocumentHelper.parseText(xml);
//            Element root = document.getRootElement();
//
//            //消息发送者/接收者
//            String fromUserName = root.elementTextTrim("FromUserName");
//            // 公众帐号
//            String toUserName = root.elementTextTrim("ToUserName");
//            //Create Time
//            String createTime = root.elementTextTrim("CreateTime");
//            // 消息类型
//            String msgType = root.elementTextTrim("MsgType");
//            String msgId = root.elementTextTrim("MsgID");
//
//            //消息key,判断是否在15秒内收到过该消息,收到过直接返回
//            String msgKey = Objects.isNull(msgId) ? (fromUserName + "_" + createTime) : msgId;
//            if (Objects.nonNull(redisUtil.get(msgKey))) {
//                return replyMsg;
//            }
//            //设置20秒缓存
//            redisUtil.set(msgKey, createTime, 20);
//
//            switch (WxMsgType.getByName(msgType)) {
//                //处理事件消息
//                case EVENT:
//                    String eventType = root.elementTextTrim("Event");
//                    String eventKey = root.elementTextTrim("EventKey");
//                    switch (WxEventType.getByName(eventType)) {
//                        case TEMPLATE_SEND_JOB_FINISH:
//                            //处理模版消息推送事件回调
//                            break;
//
//                        case SCAN:
//                            //处理扫码事件
//                            wxEventDealService.recordQrCodeScan(fromUserName, eventKey, Long.valueOf(createTime + "000"));
//                            break;
//
//                        case SUBSCRIBE:
//                            //用户关注事件
//                            eventKey = eventKey.replace("qrscene_", "");
//                            wxEventDealService.saveSubscribeUser(fromUserName, eventKey, Long.valueOf(createTime + "000"));
//                            break;
//
//                        case UNSUBSCRIBE:
//                            //用户取消关注事件
//                            wxEventDealService.updateUnSubscribeUser(fromUserName);
//                            break;
//                    }
//                    break;
//                case TEXT:
//                    //处理文本消息
//                    replyMsg = wxKeywordDealService.genReplyByKeyWord(fromUserName, toUserName, root.elementTextTrim("Content"));
//                    break;
//                default:
//                    //默认处理其余消息
//                    replyMsg = new TransferCustomerServiceMsg(toUserName,fromUserName).toXml();
//                    break;
//            }
//            if (validParams.isEncrypt()) {
//                replyMsg = new WXBizMsgCrypt(wxProperties.getToken(), wxProperties.getEncodingAesKey(), wxProperties.getAppId())
//                        .EncryptMsg(replyMsg, validParams.getTimestamp(), validParams.getNonce());
//            }
//
//            return replyMsg;
//        }
//    }

    /**
     * 接收微信统一消息
     *
     * @param request
     * @return string
     */
//    @PostMapping("wxReceive")
//    public String wxMessage(HttpServletRequest request) {
//        return wxService.wxMessage(request);
//    }

    @PostMapping("/wxReceive")
    public String post(String signature, String timestamp, String nonce, String echostr, HttpServletRequest request) {
        System.out.println("=============================================== post start");
        for (Object o : request.getParameterMap().keySet()){
            System.out.println(o + " = " + request.getParameter((String)o));
        }
        System.out.println("=============================================== post end");
        StringBuilder result = new StringBuilder();
        result.append("<xml>" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>" +
                "<FromUserName><![CDATA[fromUser]]></FromUserName>" +
                "<CreateTime>12345678</CreateTime>" +
                "<MsgType><![CDATA[text]]></MsgType>" +
                "<Content><![CDATA[你好]]></Content>" +
                "</xml>");
        return result.toString();
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
