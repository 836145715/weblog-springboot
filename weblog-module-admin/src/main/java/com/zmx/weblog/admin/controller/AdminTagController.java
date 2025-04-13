package com.zmx.weblog.admin.controller;

import com.zmx.weblog.admin.model.vo.tag.AddTagReqVO;
import com.zmx.weblog.admin.model.vo.tag.DeleteTagReqVO;
import com.zmx.weblog.admin.model.vo.tag.FindTagPageListReqVO;
import com.zmx.weblog.admin.model.vo.tag.SearchTagReqVO;
import com.zmx.weblog.admin.service.AdminTagService;
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
@Api(tags = "Admin 标签模块")
public class AdminTagController {

    @Autowired
    private AdminTagService tagService;

    @PostMapping("/tag/add")
    @ApiOperation(value = "添加标签")
    @ApiOperationLog(description = "添加标签")
    public Response addTag(@RequestBody @Validated AddTagReqVO addTagReqVO) {
        return tagService.addTag(addTagReqVO);
    }

    @PostMapping("/tag/delete")
    @ApiOperation(value = "删除标签")
    @ApiOperationLog(description = "删除标签")
    public Response deleteTag(@RequestBody @Validated DeleteTagReqVO req) {
        return tagService.deleteTag(req);
    }

    @PostMapping("/tag/list")
    @ApiOperation(value = "查询标签分页数据")
    @ApiOperationLog(description = "查询标签分页数据")
    public PageResponse findTagPageList(@RequestBody @Validated FindTagPageListReqVO req) {
        return tagService.findTagPageList(req);
    }

    @PostMapping("/tag/select/list")
    @ApiOperation(value = "查询标签下拉列表")
    @ApiOperationLog(description = "查询标签下拉列表")
    public Response selectList() {
        return tagService.selectList();
    }

    @PostMapping("/tag/search")
    @ApiOperation(value = "搜索标签")
    @ApiOperationLog(description = "搜索标签")
    public Response searchTag(@RequestBody @Validated SearchTagReqVO req) {
        return tagService.searchTag(req);
    }

}
