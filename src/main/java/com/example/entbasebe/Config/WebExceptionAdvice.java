package com.example.entbasebe.Config;

import com.example.entbasebe.Utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*定义了一个全局异常处理类WebExceptionAdvice，
发生RuntimeException时，会记录异常信息并返回一个描述服务器异常的结果。
 这有助于统一处理应用程序中的异常情况，并向客户端提供一致的错误响应。
 */
@Slf4j
@RestControllerAdvice
public class WebExceptionAdvice {
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error(e.toString(), e);
        return Result.fail("服务器异常");
    }
}
