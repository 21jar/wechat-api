package com.ainijar.common.exception;

import com.ainijar.common.config.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(-1)
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result bizException(BizException e) {
        log.error("业务异常:", e);
        String msg = messageSource.getMessage(e.getCode(), e.getArgs(), LocaleContextHolder.getLocale());
        return Result.fail(null, msg);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result notFoundException(RuntimeException e) {
        log.error("运行时异常:", e);
        String msg = messageSource.getMessage("error.system", null, LocaleContextHolder.getLocale());
        return Result.fail(null, msg);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result notFoundException(Exception e) {
        log.error("Exception异常:", e);
        String msg = messageSource.getMessage("error.system", null, LocaleContextHolder.getLocale());
        return Result.fail(null, msg);
    }
}
