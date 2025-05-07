package com.zmx.weblog.admin.controller;

import com.zmx.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.zmx.weblog.admin.service.AdminBlogSettingsService;
import com.zmx.weblog.common.aspect.ApiOperationLog;
import com.zmx.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Api(tags = "Admin 博客设置模块")
public class AdminBlogSettingsController {

    @Autowired
    private AdminBlogSettingsService blogSettingsService;

    @PostMapping("/blog/settings/update")
    @ApiOperation(value = "更新博客设置")
    @ApiOperationLog(description = "更新博客设置")
    public Response updateBlogSettings(@RequestBody @Validated UpdateBlogSettingsReqVO updateBlogSettingsReqVO) {
        return blogSettingsService.updateBlogSettings(updateBlogSettingsReqVO);
    }

    @GetMapping("/blog/settings/detail")
    @ApiOperation(value = "获取博客设置详情")
    @ApiOperationLog(description = "获取博客设置详情")
    public Response getBlogSettingsDetail() {
        return blogSettingsService.getBlogSettingsDetail();
    }
}