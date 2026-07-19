package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xx.platform.entity.DataModel;
import com.xx.platform.mapper.DataModelMapper;
import com.xx.platform.service.DataModelService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 数据实体模型服务实现类
 * 
 * 实现数据实体模型的分页查询、关键词搜索、批量删除等业务逻辑。
 */
@Service
public class DataModelServiceImpl extends ServiceImpl<DataModelMapper, DataModel> implements DataModelService {

    /**
     * 分页查询数据实体模型
     * 支持按名称或简介进行模糊搜索
     */
    @Override
    public IPage<DataModel> pageQuery(int page, int size, String keyword) {
        Page<DataModel> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<DataModel> wrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索：匹配名称或简介
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                .like(DataModel::getName, keyword)
                .or()
                .like(DataModel::getDescription, keyword)
            );
        }
        
        // 按创建时间降序排列
        wrapper.orderByDesc(DataModel::getCreateTime);
        return page(pageParam, wrapper);
    }

    /** 批量删除数据实体模型 */
    @Override
    public boolean batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return false;
        return removeByIds(ids);
    }
}
