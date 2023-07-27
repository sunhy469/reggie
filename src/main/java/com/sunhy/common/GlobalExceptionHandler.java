package com.sunhy.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description: 全局异常处理（基于代理模式）,捕获程序中出现的相应的异常在这里集中处理
 * @Version 1.0
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    //处理新增员工重名异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        log.error(exception.getMessage());

        //contains方法判断字符串是否包含某个字符串
        if (exception.getMessage().contains("Duplicate entry")){
            //截取字符串
            String[] split = exception.getMessage().split(" ");
            String username = split[2];
            if (exception.getMessage().contains("employee.idx_username"))
                return R.error("账号"+username+"已存在，请检查后重试-_-");
            if (exception.getMessage().contains("dish.idx_dish_name"))
                return R.error("菜品"+username+"已存在或已被删除，请检查后重试-_-");
        }
        //这个地方有问题，就是如果菜品被删除后再添加会添加不上了
        return R.error("网络请求繁忙，请稍后重试-_-");
    }

    //处理自定义异常
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception){
        log.error(exception.getMessage());
        return R.error(exception.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public R<String> exceptionHandler(FileNotFoundException exception){
//        log.error(exception.getMessage());
        return R.error("文件上传失败，请稍后重试-_-");
    }
}
