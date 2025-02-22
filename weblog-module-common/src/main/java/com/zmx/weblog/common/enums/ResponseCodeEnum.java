package com.zmx.weblog.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    // ---------------------通用异常状态码---------------------
    SYSTEM_ERROR("10000", "出错啦，服务器繁忙..."),

    // ----------- 业务异常状态码 -----------
    PRODUCT_NOT_FOUND("20000", "该产品不存在（测试使用）"),
    ;

    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;
}
