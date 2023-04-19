package com.jx.reggie.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})//加入了RestController或Controller注解会被这个处理器处理
@ResponseBody//反回json数据
@Slf4j
public class GlobalExceptionHandler {

    /**
     * SQLIntegrityConstraintViolationException异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)//是这个异常就进行下面方法
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String[] s = ex.getMessage().split(" ");
            String msg=s[2]+ "已经存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }


    /**
     * CustomException异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)//是这个异常就进行下面方法
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
