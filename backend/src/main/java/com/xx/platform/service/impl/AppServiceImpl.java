package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xx.platform.entity.WebApp;
import com.xx.platform.mapper.WebAppMapper;
import com.xx.platform.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Web应用服务实现类
 */
@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private WebAppMapper webAppMapper;

    @Override
    public Page<WebApp> getAppList(int page, int size, Integer categoryId, String keyword,
                                    String sortField, String sortOrder) {
        Page<WebApp> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<WebApp> wrapper = new LambdaQueryWrapper<>();

        // 只查询启用状态的应用
        wrapper.eq(WebApp::getStatus, 1);

        // 分类筛选
        if (categoryId != null) {
            wrapper.eq(WebApp::getCategoryId, categoryId);
        }

        // 关键词搜索（名称、简介）
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(WebApp::getName, keyword)
                    .or().like(WebApp::getDescription, keyword));
        }

        // 排序
        if ("clickCount".equals(sortField)) {
            if ("asc".equalsIgnoreCase(sortOrder)) {
                wrapper.orderByAsc(WebApp::getClickCount);
            } else {
                wrapper.orderByDesc(WebApp::getClickCount);
            }
        } else if ("name".equals(sortField)) {
            if ("asc".equalsIgnoreCase(sortOrder)) {
                wrapper.orderByAsc(WebApp::getName);
            } else {
                wrapper.orderByDesc(WebApp::getName);
            }
        } else {
            // 默认排序：排序序号升序，创建时间降序
            wrapper.orderByAsc(WebApp::getSortOrder).orderByDesc(WebApp::getCreateTime);
        }

        return webAppMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public WebApp getAppById(Integer id) {
        WebApp app = webAppMapper.selectById(id);
        if (app == null) {
            throw new RuntimeException("应用不存在");
        }
        return app;
    }

    @Override
    public void addApp(WebApp app) {
        app.setClickCount(0);
        app.setCreateTime(new Date());
        app.setUpdateTime(new Date());
        if (app.getStatus() == null) {
            app.setStatus(1);
        }
        webAppMapper.insert(app);
    }

    @Override
    public void updateApp(WebApp app) {
        app.setUpdateTime(new Date());
        webAppMapper.updateById(app);
    }

    @Override
    public void deleteApp(Integer id) {
        webAppMapper.deleteById(id);
    }

    @Override
    public void incrementClickCount(Integer id) {
        WebApp app = webAppMapper.selectById(id);
        if (app != null) {
            app.setClickCount(app.getClickCount() + 1);
            app.setUpdateTime(new Date());
            webAppMapper.updateById(app);
        }
    }
}
