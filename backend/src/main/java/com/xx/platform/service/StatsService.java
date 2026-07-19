package com.xx.platform.service;

import java.util.List;
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

    /**
     * 获取应用点击排行
     * @param limit 返回数量
     * @return 点击量最高的应用列表
     */
    List<Map<String, Object>> getTopApps(int limit);

    /**
     * 获取各应用类型下的应用数量统计
     * @return 各类型及其对应数量
     */
    List<Map<String, Object>> getAppTypeStats();
}
