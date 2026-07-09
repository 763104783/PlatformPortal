package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xx.platform.entity.ShowcaseItem;
import com.xx.platform.mapper.ShowcaseItemMapper;
import com.xx.platform.service.ShowcaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 宣贯数据服务实现类
 */
@Service
public class ShowcaseServiceImpl implements ShowcaseService {

    @Autowired
    private ShowcaseItemMapper showcaseItemMapper;

    @Override
    public List<ShowcaseItem> getShowcaseItems(String category) {
        LambdaQueryWrapper<ShowcaseItem> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(category)) {
            wrapper.eq(ShowcaseItem::getCategory, category);
        }
        wrapper.orderByAsc(ShowcaseItem::getSortOrder);
        return showcaseItemMapper.selectList(wrapper);
    }

    @Override
    public ShowcaseItem getShowcaseById(Integer id) {
        ShowcaseItem item = showcaseItemMapper.selectById(id);
        if (item == null) {
            throw new RuntimeException("数据不存在");
        }
        return item;
    }

    @Override
    public void addShowcaseItem(ShowcaseItem item) {
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        showcaseItemMapper.insert(item);
    }

    @Override
    public void updateShowcaseItem(ShowcaseItem item) {
        item.setUpdateTime(new Date());
        showcaseItemMapper.updateById(item);
    }

    @Override
    public void deleteShowcaseItem(Integer id) {
        showcaseItemMapper.deleteById(id);
    }
}
