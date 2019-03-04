package com.ainijar.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 微信模版信息对象User
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateMessage {

    private String touser;
    @JSONField(name = "template_id")
    private String templateId;
    private String url;
    private String topcolor;
    private Map<String, TemplateMessage.KeyWord> data;

    @Data
    @AllArgsConstructor
    public static class KeyWord {
        private String value;
        private String color;

        public KeyWord(String value) {
            this.value = value;
            // #FF0000红色 #173177蓝色
            this.color = "#173177";
        }
    }
}