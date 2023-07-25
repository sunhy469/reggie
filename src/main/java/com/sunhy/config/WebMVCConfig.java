package com.sunhy.config;

import com.sunhy.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
@Configuration
@Slf4j
public class WebMVCConfig implements WebMvcConfigurer {
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        MappingJackson2HttpMessageConverter mapJSON = new MappingJackson2HttpMessageConverter();
        mapJSON.setObjectMapper(new JacksonObjectMapper());
        //追加转换器，放到最前面
        converters.add(0,mapJSON);
    }
}
