package com.ainijar.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * 创建带参二维码结果
 *
 * @author hst on 2017/01/06
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeResult extends BaseResult {

    private String ticket;

    @JsonProperty("expire_seconds")
    @JSONField(name = "expire_seconds")
    private String expireSeconds;

    private String url;
}