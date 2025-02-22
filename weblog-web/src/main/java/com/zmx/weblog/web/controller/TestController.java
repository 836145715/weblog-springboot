package com.zmx.weblog.web.controller;


import com.zmx.weblog.common.aspect.ApiOperationLog;
import com.zmx.weblog.common.enums.ResponseCodeEnum;
import com.zmx.weblog.common.exception.BizException;
import com.zmx.weblog.common.utils.Response;
import com.zmx.weblog.web.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class TestController {

    @PostMapping("/test")
    @ApiOperationLog(description = "测试接口")
    public Response test(@RequestBody @Validated User user, BindingResult bindingResult) {
        //是否存在校验错误
        if(bindingResult.hasErrors()){
            String errorMsg = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("，"));
            return Response.fail(errorMsg);
        }
        return Response.success();
    }

    @PostMapping("/test2")
    public Response test2(@RequestBody @Validated User user, BindingResult bindingResult) {
       int i = 1/0;
       return Response.success();
    }
}
