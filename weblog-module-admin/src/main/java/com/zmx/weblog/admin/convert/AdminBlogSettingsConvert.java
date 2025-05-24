package com.zmx.weblog.admin.convert;

import com.zmx.weblog.admin.model.vo.blogsettings.BlogSettingsRspVO;
import com.zmx.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.zmx.weblog.common.domain.dos.BlogSettingsDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// 文件已重命名为 AdminBlogSettingsConvert.java，内容如下：
@Mapper
public interface AdminBlogSettingsConvert {
    /**
     * 初始化 convert 实例
     */
    AdminBlogSettingsConvert INSTANCE = Mappers.getMapper(AdminBlogSettingsConvert.class);

    BlogSettingsDO convertVO2DO(UpdateBlogSettingsReqVO bean);

    BlogSettingsRspVO convertDO2VO(BlogSettingsDO bean);

}