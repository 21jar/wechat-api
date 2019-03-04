package com.ainijar.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class QrCode {

    public static final String QR_SCENE = "QR_SCENE";
    public static final String QR_LIMIT_SCENE = "QR_LIMIT_SCENE";
    public static final String QR_LIMIT_STR_SCENE = "QR_LIMIT_STR_SCENE";

    @JsonProperty("action_name")
    @JSONField(name = "action_name")
    private QrCodeType actionName;

    @JsonProperty("expire_seconds")
    @JSONField(name = "expire_seconds")
    private Integer expireSeconds;

    @JsonProperty("action_info")
    @JSONField(name = "action_info")
    private ActionInfo actionInfo;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class ActionInfo {

        private Scene scene;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Scene {

        @JsonProperty("scene_str")
        @JSONField(name = "scene_str")
        private String sceneStr;

        @JsonProperty("scene_id")
        @JSONField(name = "scene_id")
        private Integer sceneId;
    }

    @Getter
    public enum QrCodeType {
        QR_SCENE("QR_SCENE"),
        QR_LIMIT_SCENE("QR_LIMIT_SCENE"),
        QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE"),
        QR_STR_SCENE("QR_STR_SCENE");
        private String qrCodeType;

        QrCodeType(String qrCodeType) {
            this.qrCodeType = qrCodeType;
        }
    }
}
