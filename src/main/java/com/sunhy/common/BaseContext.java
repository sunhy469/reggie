package com.sunhy.common;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description: 基于ThreadLocal的封装工具类
 * @Version 1.0
 */
public class BaseContext {
    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    public static void setCurrenID(Long id){
        THREAD_LOCAL.set(id);
    }

    public static Long getCurrentID(){
        return THREAD_LOCAL.get();
    }
}
