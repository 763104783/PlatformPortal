package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xx.platform.entity.AlgoModel;
import com.xx.platform.mapper.AlgoModelMapper;
import com.xx.platform.service.AlgoModelService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 算法能力模型服务实现类
 * 
 * 实现算法能力模型的分页查询、关键词搜索、批量删除等业务逻辑。
 */
@Service
public class AlgoModelServiceImpl extends ServiceImpl<AlgoModelMapper, AlgoModel> implements AlgoModelService {

    /**
     * 分页查询算法能力模型
     * 支持按名称或简介进行模糊搜索
     */
    @Override
    public IPage<AlgoModel> pageQuery(int page, int size, String keyword) {
        Page<AlgoModel> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<AlgoModel> wrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索：匹配名称或简介
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                .like(AlgoModel::getName, keyword)
                .or()
                .like(AlgoModel::getDescription, keyword)
            );
        }
        
        // 按创建时间降序排列
        wrapper.orderByDesc(AlgoModel::getCreateTime);
        return page(pageParam, wrapper);
    }

    /** 批量删除算法能力模型 */
    @Override
    public boolean batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return false;
        return removeByIds(ids);
    }
}
