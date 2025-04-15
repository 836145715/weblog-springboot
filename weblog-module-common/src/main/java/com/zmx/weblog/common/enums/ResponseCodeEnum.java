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

    UNAUTHORIZED("20002", "无访问权限"),

    USER_NOT_FOUND("20003", "用户不存在！"),

    FORBIDDEN("20004", "演示账号仅支持查询操作！"),

    CATEGORY_NAME_IS_EXISTED("20005", "分类名称已存在！"),

    TAG_NAME_IS_EXISTED("20006", "标签名称已存在！"),

    FILE_UPLOAD_FAILED("20008", "文件上传失败！"),

    CATEGORY_NOT_FOUND("20009", "分类不存在！"),

    ;

    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;
}
