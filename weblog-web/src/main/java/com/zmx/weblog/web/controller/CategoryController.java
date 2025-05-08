package com.zmx.weblog.web.controller;

import com.zmx.weblog.web.model.vo.category.FindCategoryArticlePageListReqVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/article/list")
    @ApiOperation(value = "前台获取分类下文章分页数据")
    @ApiOperationLog(description = "前台获取分类下文章分页数据")
    public Response findCategoryArticlePageList(
            @RequestBody @Validated FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO) {
        return categoryService.findCategoryArticlePageList(findCategoryArticlePageListReqVO);
    }

}
