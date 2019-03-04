package com.ainijar.dto;

import java.util.Objects;

/**
 * 检查返回结果是否正确
 *
 * @author hst on 2016/12/01
 */
public class ResultCheck {
    public static final String SUCCESS_CODE = "0";
    public static final String SUCCESS_MESSAGE = "ok";

    public static boolean isSuccess(BaseResult result) {
        return Objects.nonNull(result) && (SUCCESS_CODE.equals(result.getErrCode()) || result.getErrCode() == null || "".equals(result.getErrCode()));
    }
}
