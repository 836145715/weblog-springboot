package com.zmx.weblog.admin.service;

import com.zmx.weblog.admin.model.vo.article.PublishArticleReqVO;
import com.zmx.weblog.admin.model.vo.article.UpdateArticleReqVO;
import com.zmx.weblog.admin.model.vo.article.FindArticlePageListReqVO;
import com.zmx.weblog.common.utils.Response;
import com.zmx.weblog.common.utils.PageResponse;

public interface AdminArticleService {
    /**
     * 发布文章
     * 
     * @param reqVO 发布请求参数
     * @return 发布结果
     */
    Response publishArticle(PublishArticleReqVO reqVO);

    /**
     * 删除文章
     * 
     * @param articleId 文章ID
     * @return 操作结果
     */
    Response deleteArticle(Long articleId);

    /**
     * 文章分页查询
     * 
     * @param reqVO 分页查询参数
     * @return 分页结果
     */
    PageResponse findArticlePageList(FindArticlePageListReqVO reqVO);

    /**
     * 查询文章详情
     * 
     * @param articleId 文章ID
     * @return 文章详情
     */
    Response getArticleDetail(Long articleId);

    /**
     * 更新文章
     * 
     * @param reqVO 更新请求参数
     * @return 更新结果
     */
    Response updateArticle(UpdateArticleReqVO reqVO);
}