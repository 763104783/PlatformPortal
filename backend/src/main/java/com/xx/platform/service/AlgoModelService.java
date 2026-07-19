package com.xx.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xx.platform.entity.AlgoModel;

import java.util.List;

/**
 * 算法能力模型服务接口
 * 
 * 提供算法能力模型的业务操作能力：
 * - 分页查询（支持关键词搜索）
 * - 获取详情
 * - 新增/编辑/删除
 * - 批量删除
 */
public interface AlgoModelService extends IService<AlgoModel> {

    /**
     * 分页查询算法能力模型
     * @param page 页码（从1开始）
     * @param size 每页条数
     * @param keyword 搜索关键词（匹配名称或简介）
     */
    IPage<AlgoModel> pageQuery(int page, int size, String keyword);

    /**
     * 批量删除算法能力模型
     * @param ids 要删除的ID列表
     */
    boolean batchDelete(List<Integer> ids);
}
