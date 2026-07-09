package com.xx.platform.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一捕获Controller层抛出的异常并返回友好提示
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Object> handleRuntimeException(RuntimeException e) {
        return Result.error(e.getMessage());
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e) {
        e.printStackTrace();
        return Result.error("服务器内部错误：" + e.getMessage());
    }
}
