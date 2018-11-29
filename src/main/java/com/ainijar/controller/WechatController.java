package com.ainijar.controller;

import com.ainijar.common.config.Result;
import com.ainijar.common.constant.Const;
import com.ainijar.common.constant.WxEventType;
import com.ainijar.common.constant.WxMsgType;
import com.ainijar.common.exception.BizException;
import com.ainijar.common.util.RedisUtil;
import com.ainijar.common.util.XmlUtil;
import com.ainijar.model.*;
import com.ainijar.service.WechatService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${oaUrl}")
    private String oaUrl;

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
    @ApiOperation(value = "二维码图片接口", notes = "二维码图片接口")
    public Result downloadQrCode(HttpServletRequest request) {
        String ticket = request.getHeader("ticket");
        if (StringUtils.isBlank(ticket)) {
            throw new BizException("error.unauthorized");
        }
        String loginName = "";
        Object userObj = redisUtil.get(Const.USER_PREFIX + ticket);
        if (userObj == null) {
            loginName = restTemplate.getForObject(oaUrl + Const.OA_LOGINNAME_URL + "?ticket=" + ticket, String.class);
        } else {
            JSONObject json = JSONObject.parseObject(userObj.toString());
            loginName = json.getString("loginName");
        }
        QrCode qrCode = new QrCode();
        qrCode.setActionName(QrCode.QrCodeType.QR_STR_SCENE);
        qrCode.setExpireSeconds(2592000);
        QrCode.Scene scene = new QrCode.Scene();
        scene.setSceneStr(loginName);
        qrCode.setActionInfo(new QrCode.ActionInfo(scene));
        QrCodeResult qrCodeResult = wechatService.createQrCode(qrCode);
        String url = wechatService.downloadQrCode(qrCodeResult.getTicket());
        return Result.success(url, "成功");
    }

    /**
     * 处理微信消息的回调
     *
     * @param xml post消息内容
     * @return 消息处理结果
     * @throws IOException       xml读写异常
     * @throws DocumentException xml转换异常
     */
    @PostMapping("/wxReceive")
    public String receiveMessage(String signature, String timestamp, String nonce, @RequestBody(required = false) String xml, HttpServletRequest request) throws Exception {
        if (!wechatService.signature(signature, timestamp, nonce)) {
            return "failed";
        }
        String openid = request.getParameter("openid");
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        Map<String, String> map = XmlUtil.parseXml(xml);
        //消息key,判断是否在15秒内收到过该消息,收到过直接返回
        String msgKey = Objects.isNull(map.get("MsgID")) ? (map.get("FromUserName") + "_" + map.get("CreateTime")) : map.get("MsgID");
        if (redisUtil.get(msgKey) != null) {
            return "success";
        }
        //设置20秒缓存
        redisUtil.setEx(msgKey, map.get("CreateTime"), (long) 20);
        switch (WxMsgType.getByName(map.get("MsgType"))) {
            //处理事件消息
            case EVENT:
                String eventType = map.get("Event");
                switch (WxEventType.getByName(eventType)) {
                    case TEMPLATE_SEND_JOB_FINISH:
                        //处理模版消息推送事件回调
                        log.info("处理模版消息推送事件回调");
                        break;
                    case SCAN:
                        log.info("处理扫码事件");
                        break;
                    case SUBSCRIBE:
                        //用户关注事件
                        log.info("用户关注事件");
                        String eventKey = map.get("EventKey").split("_")[1];
                        // 绑定用户和openid
                        Query query = new Query();
                        query.addCriteria(Criteria.where("loginName").is(eventKey));
                        Update update = Update.update("openid", openid);
                        mongoTemplate.upsert(query, update, Const.ANHUI_USER);
                        break;
                    case UNSUBSCRIBE:
                        //用户取消关注事件
                        log.info("用户取消关注事件");
                        Query query1 = new Query();
                        query1.addCriteria(Criteria.where("openid").is(openid));
                        Update update1 = Update.update("openid", null);
                        mongoTemplate.upsert(query1, update1, Const.ANHUI_USER);
                        break;
                    default:
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
