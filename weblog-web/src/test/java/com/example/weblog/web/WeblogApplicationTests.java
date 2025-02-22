package com.example.weblog.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = WeblogApplicationTests.class)
@Slf4j
public class WeblogApplicationTests {

    @Test
    void contextLoads() {
        log.info("测试");
    }

    @Test
    void testLog(){
        log.info("这是一行Info级别日志");
        log.warn("这是一行Warn级别日志");
        log.error("这是一行Error级别日志");

        //占位符
        String author = "zmx";
        log.info("作者的名字是{}", author);
    }

}
