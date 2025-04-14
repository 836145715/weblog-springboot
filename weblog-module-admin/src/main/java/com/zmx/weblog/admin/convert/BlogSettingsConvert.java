package com.zmx.weblog.admin.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.zmx.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.zmx.weblog.common.domain.dos.BlogSettingsDO;

@Mapper
public interface BlogSettingsConvert {
    /**
     * 初始化convert 实例
     * 
     */
    BlogSettingsConvert INSTANCE = Mappers.getMapper(BlogSettingsConvert.class);

    /**
     * 
     */
    BlogSettingsDO convertVO2DO(UpdateBlogSettingsReqVO bean);

}
