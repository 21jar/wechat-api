package com.ainijar.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WeixinTemplateMsg<E> {
    private String touser;
    @JSONField(name = "template_id")
    private String templateId;
    private String url;
    private String topcolor;
    private E data;
}
