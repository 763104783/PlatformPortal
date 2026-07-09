package com.xx.platform.service;

import java.util.Map;

/**
 * 统计服务接口
 */
public interface StatsService {

    /**
     * 获取平台总览统计数据
     * @return 包含收录应用数、总点击数、用户数、分类数等统计信息
     */
    Map<String, Object> getOverviewStats();
}
