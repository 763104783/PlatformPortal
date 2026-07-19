package com.xx.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xx.platform.entity.DataModel;

import java.util.List;

/**
 * 数据实体模型服务接口
 * 
 * 提供数据实体模型的业务操作能力：
 * - 分页查询（支持关键词搜索）
 * - 获取详情
 * - 新增/编辑/删除
 * - 批量删除
 */
public interface DataModelService extends IService<DataModel> {

    /**
     * 分页查询数据实体模型
     * @param page 页码（从1开始）
     * @param size 每页条数
     * @param keyword 搜索关键词（匹配名称或简介）
     * @return 分页结果
     */
    IPage<DataModel> pageQuery(int page, int size, String keyword);

    /**
     * 批量删除数据实体模型
     * @param ids 要删除的ID列表
     */
    boolean batchDelete(List<Integer> ids);
}
