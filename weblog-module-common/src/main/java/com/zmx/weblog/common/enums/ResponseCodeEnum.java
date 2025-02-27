package com.zmx.weblog.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    // ---------------------通用异常状态码---------------------
    SYSTEM_ERROR("10000", "出错啦，服务器繁忙..."),

    PARAM_NOT_VALID("10001", "参数校验失败"),

    LOGIN_FAIL("20000", "登录失败"),
    USERNAME_OR_PWD_ERROR("20001", "用户名或密码错误"),
    ;

    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;
}
