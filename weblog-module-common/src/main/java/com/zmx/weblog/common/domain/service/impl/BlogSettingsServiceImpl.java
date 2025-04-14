package com.zmx.weblog.common.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.weblog.common.domain.dos.BlogSettingsDO;
import com.zmx.weblog.common.domain.mapper.BlogSettingsMapper;
import com.zmx.weblog.common.domain.service.BlogSettingsService;
import org.springframework.stereotype.Service;

@Service
public class BlogSettingsServiceImpl extends ServiceImpl<BlogSettingsMapper, BlogSettingsDO>
        implements BlogSettingsService {
}