package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xx.platform.entity.ProcessTemplate;
import com.xx.platform.mapper.ProcessTemplateMapper;
import com.xx.platform.service.ProcessTemplateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务流程模板服务实现类
 */
@Service
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate> implements ProcessTemplateService {

    /** 获取所有流程模板列表，按排序号升序 */
    @Override
    public List<ProcessTemplate> listAll() {
        LambdaQueryWrapper<ProcessTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ProcessTemplate::getSortOrder);
        return list(wrapper);
    }

    /** 批量删除流程模板 */
    @Override
    public boolean batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return false;
        return removeByIds(ids);
    }
}
