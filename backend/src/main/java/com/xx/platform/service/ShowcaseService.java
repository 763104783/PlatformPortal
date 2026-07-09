package com.xx.platform.service;

import com.xx.platform.entity.ShowcaseItem;

import java.util.List;

/**
 * 宣贯数据服务接口
 */
public interface ShowcaseService {

    /**
     * 按类别获取宣贯项列表
     * @param category 类别（可选，为空则获取全部）
     */
    List<ShowcaseItem> getShowcaseItems(String category);

    /**
     * 获取宣贯项详情
     */
    ShowcaseItem getShowcaseById(Integer id);

    /**
     * 新增宣贯项
     */
    void addShowcaseItem(ShowcaseItem item);

    /**
     * 更新宣贯项
     */
    void updateShowcaseItem(ShowcaseItem item);

    /**
     * 删除宣贯项
     */
    void deleteShowcaseItem(Integer id);
}
