package com.xx.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xx.platform.entity.BizProcess;

import java.util.List;

/**
 * 业务流程服务接口
 * 
 * 提供业务流程的业务操作能力：
 * - 分页查询（支持关键词搜索，返回关联的模板信息）
 * - 获取详情（含关联模板列表）
 * - 新增（同时保存模板关联关系）
 * - 编辑（同时更新模板关联关系）
 * - 删除（同时删除模板关联关系）
 * - 批量删除
 */
public interface BizProcessService extends IService<BizProcess> {

    /**
     * 分页查询业务流程（含关联模板信息）
     * @param page 页码
     * @param size 每页条数
     * @param keyword 搜索关键词
     */
    IPage<BizProcess> pageQuery(int page, int size, String keyword);

    /**
     * 获取业务流程详情（含关联模板列表）
     * @param id 流程ID
     */
    BizProcess getDetail(Integer id);

    /**
     * 新增业务流程（同时保存模板关联）
     * @param process 流程信息（含templateIds）
     */
    boolean addProcess(BizProcess process);

    /**
     * 编辑业务流程（同时更新模板关联）
     * @param process 流程信息（含templateIds）
     */
    boolean updateProcess(BizProcess process);

    /**
     * 批量删除业务流程（同时删除关联关系）
     * @param ids 要删除的ID列表
     */
    boolean batchDelete(List<Integer> ids);
}
