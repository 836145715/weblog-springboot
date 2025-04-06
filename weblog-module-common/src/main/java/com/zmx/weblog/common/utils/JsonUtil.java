package com.zmx.weblog.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JsonUtil implements InitializingBean {

    private static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        JsonUtil.objectMapper = objectMapper;
    }

    public static String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("json序列化出错：" + obj, e);
            return null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (objectMapper == null) {
            throw new IllegalStateException("ObjectMapper 未正确注入！");
        }
    }
}
