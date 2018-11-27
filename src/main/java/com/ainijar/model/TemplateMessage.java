package com.ainijar.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

/**
 * 微信模版信息对象
 *
 * @author hst on 2016/11/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateMessage {

    @JsonProperty("touser")
    @JSONField(name = "touser")
    private String toUser;

    @JsonProperty("template_id")
    @JSONField(name = "template_id")
    private String templateId;

    private String url;

    private Map<String, KeyWord> data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeyWord {
        private String value;
        private String color;

        public KeyWord(String value) {
            this.value = value;
            this.color = "#173177";
        }
    }
}