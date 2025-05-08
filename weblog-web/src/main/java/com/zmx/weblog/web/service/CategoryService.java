package com.zmx.weblog.web.service;

import com.zmx.weblog.common.utils.Response;
import com.zmx.weblog.web.model.vo.category.FindCategoryArticlePageListReqVO;

public interface CategoryService {
    Response findCategoryList();

    /**
     * 获取分类下文章分页数据
     * @param findCategoryArticlePageListReqVO
     * @return
     */
    Response findCategoryArticlePageList(FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO);
}
