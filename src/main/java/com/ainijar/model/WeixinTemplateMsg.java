package com.ainijar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeixinTemplateMsg<E> {
    private String touser;
    @JsonProperty("template_id")
    private String templateId;
    private String url;
    private String topcolor;
    private E data;
}
