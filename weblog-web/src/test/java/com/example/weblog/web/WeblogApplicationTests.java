package com.example.weblog.web;

import com.zmx.weblog.common.domain.dos.UserDO;
import com.zmx.weblog.common.domain.mapper.UserMapper;
import com.zmx.weblog.web.WeblogWebApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;

@SpringBootTest(classes = WeblogWebApplication.class)
@Slf4j
public class WeblogApplicationTests {

    @Autowired
    private UserMapper userMapper; // 确保 userMapper 被正确注入

    @Test
    void inertTest() {
        // 构建数据库实体类
        UserDO userDO = UserDO.builder()
                .username("犬小哈")
                .password("123456")
                .createTime(new Date())
                .updateTime(new Date())
                .isDeleted(false)
                .build();

        userMapper.insert(userDO);
    }
}
