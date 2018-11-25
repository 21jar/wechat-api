package com.ainijar.common.config;

import lombok.Data;

/**
 * @author slt
 * @date 2018/10/19
 */
@Data
public class Result<T> {

    private static final int SUCCESS = 1;

    private static final int FAIL = 0;

    private T data;

    private String msg;

    private int state;

    private Result (T data, String msg, int state) {
        this.data = data;
        this.msg = msg;
        this.state = state;
    }

    public static <T> Result<T> success (T data, String msg) {
        return new Result(data, msg, SUCCESS);
    }

    public static <T> Result<T> fail (T data, String msg) {
        return new Result(data, msg, FAIL);
    }

}
