package com.zmx.weblog.admin.service.impl;

import com.zmx.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.zmx.weblog.admin.service.AdminBlogSettingsService;
import com.zmx.weblog.common.domain.dos.BlogSettingsDO;
import com.zmx.weblog.common.domain.service.BlogSettingsService;
import com.zmx.weblog.common.utils.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminBlogSettingsServiceImpl implements AdminBlogSettingsService {

    @Autowired
    private BlogSettingsService blogSettingsService;

    @Override
    public Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO) {
        // 将 VO 转换为 DO
        BlogSettingsDO blogSettingsDO = new BlogSettingsDO();
        BeanUtils.copyProperties(updateBlogSettingsReqVO, blogSettingsDO);

        blogSettingsDO.setId(1L);
        // 更新数据库
        blogSettingsService.saveOrUpdate(blogSettingsDO);

        return Response.success();
    }

    @Override
    public Response getBlogSettingsDetail() {
        // 获取博客设置详情
        BlogSettingsDO blogSettingsDO = blogSettingsService.getById(1L);
        return Response.success(blogSettingsDO);
    }
}