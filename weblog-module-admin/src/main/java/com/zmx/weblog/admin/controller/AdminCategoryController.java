package com.zmx.weblog.admin.controller;

import com.zmx.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.zmx.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.zmx.weblog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.zmx.weblog.admin.service.AdminCategoryService;
import com.zmx.weblog.common.aspect.ApiOperationLog;
import com.zmx.weblog.common.utils.PageResponse;
import com.zmx.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Api(tags = "Admin 分类模块")
public class AdminCategoryController {

    @Autowired
    private AdminCategoryService categoryService;

    @PostMapping("/category/add")
    @ApiOperation(value = "添加分类")
    @ApiOperationLog(description = "添加分类")
    public Response addCategory(@RequestBody @Validated AddCategoryReqVO addCategoryReqVO) {
        return categoryService.addCategory(addCategoryReqVO);
    }

    @PostMapping("/category/list")
    @ApiOperation(value = "查询分类分页数据")
    @ApiOperationLog(description = "查询分类分页数据")
    public PageResponse findCategoryList(@RequestBody @Validated FindCategoryPageListReqVO req){
        return categoryService.findCategoryPageList(req);
    }

    @PostMapping("/category/delete")
    @ApiOperation(value = "删除分类")
    @ApiOperationLog(description = "删除分类")
    public Response deleteCategory(@RequestBody @Validated DeleteCategoryReqVO req){
        return categoryService.deleteCategory(req);
    }


}
