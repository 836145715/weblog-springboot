package com.zmx.weblog.admin.service;

import com.zmx.weblog.admin.model.vo.article.PublishArticleReqVO;
import com.zmx.weblog.common.utils.Response;

public interface ArticleService {
    /**
     * 发布文章
     * 
     * @param reqVO 发布请求参数
     * @return 发布结果
     */
    Response publishArticle(PublishArticleReqVO reqVO);
}