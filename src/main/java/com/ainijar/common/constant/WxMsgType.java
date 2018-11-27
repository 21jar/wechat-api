package com.ainijar.common.constant;

/**
 * Created by 胡超云 on 2016/11/28.
 */
public enum WxMsgType {
    // 文本消息
    TEXT("text"),
    // 返回消息类型：音乐
    MUSIC("music"),
    // 返回消息类型：图文
    NEWS("news"),
    // 返回消息类型：图片
    IMAGE("image"),
    // 返回消息类型：视频
    VIDEO("video"),
    // 返回消息类型：语音
    VOICE("voice"),
    // 请求消息类型：链接
    LINK("link"),
    // 请求消息类型：地理位置
    LOCATION("location"),
    // 请求消息类型：MP图文
    MPNEWS("mpnews"),
    // 请求消息类型：推送
    EVENT("event"),
  //转接客服消息
    TRANSFERCUSTOMERSERVICE("transfer_customer_service"),
    //other
    OTHER("other");
    

    private String name;

    WxMsgType(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public static WxMsgType getByName(String name) {
        for (WxMsgType type : WxMsgType.values()) {
            if (type.getName().equals(name))
                return type;
        }
        return WxMsgType.OTHER;
    }
}
