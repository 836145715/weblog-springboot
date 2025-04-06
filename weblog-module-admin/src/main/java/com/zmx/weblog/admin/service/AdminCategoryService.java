package com.zmx.weblog.admin.service;

import com.zmx.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.zmx.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.zmx.weblog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.zmx.weblog.common.utils.PageResponse;
import com.zmx.weblog.common.utils.Response;

public interface AdminCategoryService {
    /**
     * 添加分类
     * @param addCategoryReqVO
     * @return
     */
    Response addCategory(AddCategoryReqVO addCategoryReqVO);

    PageResponse findCategoryPageList(FindCategoryPageListReqVO req);

    Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO);

    Response findCategorySelectList();

}
