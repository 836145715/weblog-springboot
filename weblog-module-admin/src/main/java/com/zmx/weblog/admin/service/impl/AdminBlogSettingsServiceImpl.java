package com.zmx.weblog.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.weblog.admin.convert.BlogSettingsConvert;
import com.zmx.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.zmx.weblog.admin.service.AdminBlogSettingsService;
import com.zmx.weblog.common.domain.dos.BlogSettingsDO;
import com.zmx.weblog.common.domain.mapper.BlogSettingsMapper;
import com.zmx.weblog.common.utils.Response;
import org.springframework.stereotype.Service;

@Service
public class AdminBlogSettingsServiceImpl extends ServiceImpl<BlogSettingsMapper, BlogSettingsDO> implements AdminBlogSettingsService {


    @Override
    public Response updateBlogSettings(UpdateBlogSettingsReqVO updateBlogSettingsReqVO) {
        // 将 VO 转换为 DO
        BlogSettingsDO blogSettingsDO = BlogSettingsConvert.INSTANCE.convertVO2DO(updateBlogSettingsReqVO);
        blogSettingsDO.setId(1L);
        // 更新数据库
        saveOrUpdate(blogSettingsDO);
        return Response.success();
    }

    @Override
    public Response getBlogSettingsDetail() {
        // 获取博客设置详情
        BlogSettingsDO blogSettingsDO = getById(1L);
        return Response.success(blogSettingsDO);
    }
}