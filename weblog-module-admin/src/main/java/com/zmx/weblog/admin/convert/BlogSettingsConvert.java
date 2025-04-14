package com.zmx.weblog.admin.convert;

import com.zmx.weblog.admin.model.vo.blogsettings.BlogSettingsRspVO;
import com.zmx.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.zmx.weblog.common.domain.dos.BlogSettingsDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlogSettingsConvert {
    /**
     * 初始化 convert 实例
     */
    BlogSettingsConvert INSTANCE = Mappers.getMapper(BlogSettingsConvert.class);

    BlogSettingsDO convertVO2DO(UpdateBlogSettingsReqVO bean);

    BlogSettingsRspVO convertDO2VO(BlogSettingsDO bean);

}
