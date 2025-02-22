package com.zmx.weblog.common.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    private static final ObjectMapper INSTANCE = new ObjectMapper();

    public static String toJsonString(Object obj){
        try {
            return INSTANCE.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("json序列化出错：" + obj, e);
            return null;
        }
    }

}
