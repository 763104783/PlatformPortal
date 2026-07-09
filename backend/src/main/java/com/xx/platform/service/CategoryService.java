package com.xx.platform.service;

import com.xx.platform.entity.AppCategory;

import java.util.List;

/**
 * 应用分类服务接口
 */
public interface CategoryService {

    /**
     * 获取所有分类（按排序序号排列）
     */
    List<AppCategory> getAllCategories();

    /**
     * 新增分类
     */
    void addCategory(AppCategory category);

    /**
     * 更新分类
     */
    void updateCategory(AppCategory category);

    /**
     * 删除分类
     */
    void deleteCategory(Integer id);
}
