package com.sunhy.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.prefs.BackingStoreException;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description: 自动填充策略
 * @Version 1.0
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    // 插入时的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        log.info("添加人ID："+BaseContext.getCurrentID());
        metaObject.setValue("createUser", BaseContext.getCurrentID());
        metaObject.setValue("updateUser", BaseContext.getCurrentID());
    }

    // 更新时的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        log.info("添加人ID："+BaseContext.getCurrentID());
        metaObject.setValue("updateUser", BaseContext.getCurrentID());
    }
}
