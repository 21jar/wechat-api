package com.ainijar.common.exception;

import java.util.Arrays;

public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    private String[] args;

    public BizException(String code, String... args) {
        super(code + ", " + Arrays.toString(args));
        this.code = code;
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    public String[] getArgs() {
        return args;
    }
}
