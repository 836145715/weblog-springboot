package com.zmx.weblog.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * 签发人
     */
    private String issuer = "zmx";

    /**
     * 秘钥
     */
    private String secret = "YxF4Es8DzGXcIrFQqZGzg3EkV1CasbxToLxariE4Fm0G8smKjsuAuj4l3d7DBR4zGCk/9YBZuW2nL0qxYFfbfw==";

    /**
     * Token 失效时间（分钟）
     */
    private Long tokenExpireTime = 1440L;

    /**
     * token 请求头中的 key 值
     */
    private String tokenHeaderKey = "Authorization";

    /**
     * token 请求头中的 value 值前缀
     */
    private String tokenPrefix = "Bearer";
}