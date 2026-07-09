package com.xx.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xx.platform.entity.WebApp;

/**
 * Web应用服务接口
 */
public interface AppService {

    /**
     * 分页查询应用列表（支持筛选、排序）
     * @param page 页码
     * @param size 每页数量
     * @param categoryId 分类ID（可选）
     * @param keyword 搜索关键词（可选）
     * @param sortField 排序字段（可选）
     * @param sortOrder 排序方向 asc/desc（可选）
     */
    Page<WebApp> getAppList(int page, int size, Integer categoryId, String keyword, String sortField, String sortOrder);

    /**
     * 获取应用详情
     */
    WebApp getAppById(Integer id);

    /**
     * 新增应用
     */
    void addApp(WebApp app);

    /**
     * 更新应用
     */
    void updateApp(WebApp app);

    /**
     * 删除应用
     */
    void deleteApp(Integer id);

    /**
     * 增加点击次数
     */
    void incrementClickCount(Integer id);
}
