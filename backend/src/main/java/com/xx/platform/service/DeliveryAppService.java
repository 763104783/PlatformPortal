package com.xx.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xx.platform.entity.DeliveryApp;

import java.util.List;

/**
 * 交付应用服务接口
 * 
 * 提供交付应用（业务APP）的业务操作能力：
 * - 分页查询（支持关键词搜索、类型筛选、排序）
 * - 获取详情（含关联的应用类型和业务流程）
 * - 新增（同时保存流程关联关系）
 * - 编辑（同时更新流程关联关系）
 * - 删除（同时删除流程关联关系）
 * - 批量删除
 * - 记录点击次数
 */
public interface DeliveryAppService extends IService<DeliveryApp> {

    /**
     * 分页查询交付应用（前台展示用，只查启用状态）
     * @param page 页码
     * @param size 每页条数
     * @param keyword 搜索关键词（匹配名称或简介）
     * @param typeId 应用类型ID筛选（可选）
     * @param sortField 排序字段：clickCount/name/sortOrder
     */
    IPage<DeliveryApp> pageQuery(int page, int size, String keyword, Integer typeId, String sortField);

    /**
     * 分页查询交付应用（后台管理用，查所有状态）
     */
    IPage<DeliveryApp> pageQueryAll(int page, int size, String keyword, Integer typeId);

    /**
     * 获取应用详情（含关联信息）
     * @param id 应用ID
     */
    DeliveryApp getDetail(Integer id);

    /**
     * 新增交付应用（同时保存流程关联）
     * @param app 应用信息（含processIds）
     */
    boolean addApp(DeliveryApp app);

    /**
     * 编辑交付应用（同时更新流程关联）
     * @param app 应用信息（含processIds）
     */
    boolean updateApp(DeliveryApp app);

    /**
     * 批量删除交付应用（同时删除关联关系）
     * @param ids 要删除的ID列表
     */
    boolean batchDelete(List<Integer> ids);

    /**
     * 记录应用点击次数（点击数+1）
     * @param id 应用ID
     */
    void incrementClickCount(Integer id);
}
