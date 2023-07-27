package com.sunhy.common;

import org.springframework.stereotype.Component;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
public class CustomException extends RuntimeException  {
    public CustomException(String message) {
        super(message);
    }
}
