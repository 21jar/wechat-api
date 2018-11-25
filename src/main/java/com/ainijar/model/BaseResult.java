package com.ainijar.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信错误信息类
 *
 * @author hst on 2016/11/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResult {

    @JsonProperty("errcode")
    @JSONField(name = "errcode")
    private String errCode;
    @JsonProperty("errmsg")
    @JSONField(name = "errmsg")
    private String errMsg;
}
