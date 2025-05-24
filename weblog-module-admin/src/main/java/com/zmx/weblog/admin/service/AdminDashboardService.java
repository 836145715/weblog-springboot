package com.zmx.weblog.admin.service;

import com.zmx.weblog.common.utils.Response;

public interface AdminDashboardService {

    /**
     * 获取仪表盘基础统计数据
     * @return
     */
    Response findDashboardStatistics();

    /**
     * 获取文章发布热点统计信息
     * @return
     */
    Response findDashboardPublishArticleStatistics();
}
