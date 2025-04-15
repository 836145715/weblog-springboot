package com.zmx.weblog.admin.controller;

import com.zmx.weblog.admin.model.vo.article.PublishArticleReqVO;
import com.zmx.weblog.admin.model.vo.article.DeleteArticleReqVO;
import com.zmx.weblog.admin.model.vo.article.FindArticlePageListReqVO;
import com.zmx.weblog.admin.service.ArticleService;
import com.zmx.weblog.common.utils.Response;
import com.zmx.weblog.common.utils.PageResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/article")
@Api(tags = "Admin 文章管理")

@Validated
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/publish")
    @ApiOperation("发布文章")
    public Response publishArticle(@Valid @RequestBody PublishArticleReqVO reqVO) {
        return articleService.publishArticle(reqVO);
    }

    @PostMapping("/delete")
    @ApiOperation("删除文章")
    public Response deleteArticle(@RequestBody DeleteArticleReqVO reqVO) {
        return articleService.deleteArticle(reqVO.getArticleId());
    }

    @PostMapping("/list")
    @ApiOperation("查询文章分页数据")
    public PageResponse findArticlePageList(@RequestBody @Validated FindArticlePageListReqVO req) {
        return articleService.findArticlePageList(req);
    }

    @PostMapping("/detail")
    @ApiOperation("查询文章详情")
    public Response getArticleDetail(@RequestBody @Validated DeleteArticleReqVO reqVO) {
        return articleService.getArticleDetail(reqVO.getArticleId());
    }
}