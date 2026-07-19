package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xx.platform.entity.AppType;
import com.xx.platform.mapper.AppTypeMapper;
import com.xx.platform.service.AppTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应用类型服务实现类
 * 
 * 实现应用类型的增删改查业务逻辑。
 * 继承ServiceImpl获得MyBatis-Plus的通用CRUD实现。
 */
@Service
public class AppTypeServiceImpl extends ServiceImpl<AppTypeMapper, AppType> implements AppTypeService {

    /**
     * 获取所有应用类型列表
     * 按排序号(sort_order)升序排列
     */
    @Override
    public List<AppType> listAll() {
        LambdaQueryWrapper<AppType> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(AppType::getSortOrder);
        return list(wrapper);
    }

    /**
     * 批量删除应用类型
     * @param ids 要删除的ID列表
     * @return 删除成功后的结果
     */
    @Override
    public boolean batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return removeByIds(ids);
    }
}
