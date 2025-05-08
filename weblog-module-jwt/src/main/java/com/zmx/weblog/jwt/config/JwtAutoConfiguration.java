package com.zmx.weblog.jwt.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@ComponentScan("com.zmx.weblog.jwt")
public class JwtAutoConfiguration {
    // 自动配置类，启用JwtProperties配置
}