package com.zmx.weblog.admin.service;

import com.zmx.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.zmx.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.zmx.weblog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.zmx.weblog.admin.model.vo.category.SearchCateReqVO;
import com.zmx.weblog.common.utils.PageResponse;
import com.zmx.weblog.common.utils.Response;

public interface AdminCategoryService {
    /**
     * 添加分类
     * 
     * @param addCategoryReqVO
     * @return
     */
    Response addCategory(AddCategoryReqVO addCategoryReqVO);

    /**
     * 查询分类分页列表
     * 
     * @param req
     * @return
     */
    PageResponse findCategoryPageList(FindCategoryPageListReqVO req);

    /**
     * 删除分类
     * 
     * @param deleteCategoryReqVO
     * @return
     */
    Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO);

    /**
     * 查询分类下拉列表
     * 
     * @return
     */
    Response selectList();

    /**
     * 搜索分类
     * 
     * @param searchCateReqVO
     * @return
     */
    Response searchSelectList(SearchCateReqVO searchCateReqVO);

}
