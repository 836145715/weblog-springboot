package com.zmx.weblog.web.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zmx.weblog.common.aspect.ApiOperationLog;
import com.zmx.weblog.common.utils.Response;
import com.zmx.weblog.web.service.CategoryService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/category")
@Api(tags = "分类")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @ApiOperation("获取分类列表")
    @ApiOperationLog(description = "获取分类列表")
    public Response findCategoryList() {
        return categoryService.findCategoryList();
    }

}
