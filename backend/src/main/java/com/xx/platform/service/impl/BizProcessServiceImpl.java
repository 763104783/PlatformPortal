package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xx.platform.entity.BizProcess;
import com.xx.platform.entity.ProcessTemplate;
import com.xx.platform.entity.ProcessTemplateRel;
import com.xx.platform.mapper.BizProcessMapper;
import com.xx.platform.mapper.ProcessTemplateMapper;
import com.xx.platform.mapper.ProcessTemplateRelMapper;
import com.xx.platform.service.BizProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务流程服务实现类
 * 
 * 实现业务流程的增删改查，并维护与流程模板的多对多关联关系。
 * 关联关系通过中间表 process_template_rel 维护。
 */
@Service
public class BizProcessServiceImpl extends ServiceImpl<BizProcessMapper, BizProcess> implements BizProcessService {

    @Autowired
    private ProcessTemplateRelMapper templateRelMapper;

    @Autowired
    private ProcessTemplateMapper templateMapper;

    /**
     * 分页查询业务流程
     * 每条记录会填充其关联的模板列表
     */
    @Override
    public IPage<BizProcess> pageQuery(int page, int size, String keyword) {
        Page<BizProcess> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<BizProcess> wrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索：匹配名称或简介
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                .like(BizProcess::getName, keyword)
                .or()
                .like(BizProcess::getDescription, keyword)
            );
        }
        
        wrapper.orderByDesc(BizProcess::getCreateTime);
        IPage<BizProcess> result = page(pageParam, wrapper);
        
        // 为每条记录填充关联的模板信息
        for (BizProcess process : result.getRecords()) {
            process.setTemplates(getTemplatesByProcessId(process.getId()));
        }
        
        return result;
    }

    /**
     * 获取业务流程详情（含关联模板列表）
     */
    @Override
    public BizProcess getDetail(Integer id) {
        BizProcess process = getById(id);
        if (process != null) {
            process.setTemplates(getTemplatesByProcessId(id));
        }
        return process;
    }

    /**
     * 新增业务流程
     * 1. 保存流程基本信息
     * 2. 保存流程与模板的关联关系
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addProcess(BizProcess process) {
        // 保存流程基本信息
        boolean saved = save(process);
        
        // 保存模板关联关系
        if (saved && process.getTemplateIds() != null && !process.getTemplateIds().isEmpty()) {
            saveTemplateRelations(process.getId(), process.getTemplateIds());
        }
        
        return saved;
    }

    /**
     * 编辑业务流程
     * 1. 更新流程基本信息
     * 2. 删除旧的模板关联，保存新的关联
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProcess(BizProcess process) {
        // 更新流程基本信息
        boolean updated = updateById(process);
        
        // 更新模板关联关系：先删后增
        if (updated && process.getTemplateIds() != null) {
            // 删除旧的关联
            deleteTemplateRelations(process.getId());
            // 保存新的关联
            if (!process.getTemplateIds().isEmpty()) {
                saveTemplateRelations(process.getId(), process.getTemplateIds());
            }
        }
        
        return updated;
    }

    /**
     * 批量删除业务流程
     * 同时删除相关的模板关联关系
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return false;
        
        // 删除每个流程的模板关联
        for (Integer id : ids) {
            deleteTemplateRelations(id);
        }
        
        // 删除流程本身
        return removeByIds(ids);
    }

    /**
     * 根据流程ID获取关联的模板列表
     * @param processId 流程ID
     * @return 模板列表
     */
    private List<ProcessTemplate> getTemplatesByProcessId(Integer processId) {
        // 查询关联关系
        LambdaQueryWrapper<ProcessTemplateRel> relWrapper = new LambdaQueryWrapper<>();
        relWrapper.eq(ProcessTemplateRel::getProcessId, processId);
        List<ProcessTemplateRel> relations = templateRelMapper.selectList(relWrapper);
        
        if (relations.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取模板ID列表
        List<Integer> templateIds = relations.stream()
            .map(ProcessTemplateRel::getTemplateId)
            .collect(Collectors.toList());
        
        // 查询模板详情
        return templateMapper.selectBatchIds(templateIds);
    }

    /**
     * 保存流程与模板的关联关系
     * @param processId 流程ID
     * @param templateIds 模板ID列表
     */
    private void saveTemplateRelations(Integer processId, List<Integer> templateIds) {
        for (Integer templateId : templateIds) {
            ProcessTemplateRel rel = new ProcessTemplateRel();
            rel.setProcessId(processId);
            rel.setTemplateId(templateId);
            templateRelMapper.insert(rel);
        }
    }

    /**
     * 删除流程的所有模板关联关系
     * @param processId 流程ID
     */
    private void deleteTemplateRelations(Integer processId) {
        LambdaQueryWrapper<ProcessTemplateRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessTemplateRel::getProcessId, processId);
        templateRelMapper.delete(wrapper);
    }
}
