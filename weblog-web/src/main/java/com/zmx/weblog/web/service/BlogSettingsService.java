package com.zmx.weblog.web.service;

import com.zmx.weblog.common.utils.Response;

public interface BlogSettingsService {
    /**
     * 获取博客设置信息
     * @return
     */
    Response findDetail();
}
