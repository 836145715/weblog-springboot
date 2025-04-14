package com.zmx.weblog.admin.service;

import com.zmx.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.zmx.weblog.common.utils.Response;

public interface AdminBlogSettingsService {
    /**
     * 更新博客设置
     */
    Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO);

    /**
     * 获取博客设置详情
     */
    Response getBlogSettingsDetail();
}