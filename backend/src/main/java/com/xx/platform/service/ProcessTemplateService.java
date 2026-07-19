package com.xx.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xx.platform.entity.ProcessTemplate;

import java.util.List;

/**
 * 业务流程模板服务接口
 * 
 * 提供业务流程模板（枚举量）的业务操作能力。
 */
public interface ProcessTemplateService extends IService<ProcessTemplate> {

    /**
     * 获取所有流程模板列表（按排序号升序）
     */
    List<ProcessTemplate> listAll();

    /**
     * 批量删除流程模板
     * @param ids 要删除的ID列表
     */
    boolean batchDelete(List<Integer> ids);
}
