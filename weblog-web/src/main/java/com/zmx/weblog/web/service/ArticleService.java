package com.zmx.weblog.web.service;

import com.zmx.weblog.common.utils.Response;
import com.zmx.weblog.web.model.vo.category.FindIndexArticlePageListReqVO;

public interface ArticleService  {
    /**
     * 获取首页文章分页数据
     * @param findIndexArticlePageListReqVO
     * @return
     */
    Response findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO);
}
