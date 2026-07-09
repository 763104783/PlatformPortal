package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xx.platform.entity.AppCategory;
import com.xx.platform.mapper.AppCategoryMapper;
import com.xx.platform.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 应用分类服务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private AppCategoryMapper categoryMapper;

    @Override
    public List<AppCategory> getAllCategories() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<AppCategory>().orderByAsc(AppCategory::getSortOrder));
    }

    @Override
    public void addCategory(AppCategory category) {
        category.setCreateTime(new Date());
        categoryMapper.insert(category);
    }

    @Override
    public void updateCategory(AppCategory category) {
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryMapper.deleteById(id);
    }
}
