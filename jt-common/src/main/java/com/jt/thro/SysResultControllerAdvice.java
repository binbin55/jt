package com.jt.thro;

import com.jt.vo.SysResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice   //定义全局异常处理机制
@Slf4j
public class SysResultControllerAdvice {

    //当发生什么异常时使用该处理方式
    @ExceptionHandler(RuntimeException.class)
    public SysResult sysResultException(Exception e){
        e.printStackTrace();
        log.error("服务器异常:"+e.getMessage());
        return SysResult.fail();
    }

}
