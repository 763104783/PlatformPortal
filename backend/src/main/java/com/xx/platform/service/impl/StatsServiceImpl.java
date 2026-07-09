package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xx.platform.entity.WebApp;
import com.xx.platform.mapper.AppCategoryMapper;
import com.xx.platform.mapper.SysUserMapper;
import com.xx.platform.mapper.WebAppMapper;
import com.xx.platform.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计服务实现类
 */
@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private WebAppMapper webAppMapper;

    @Autowired
    private AppCategoryMapper categoryMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public Map<String, Object> getOverviewStats() {
        Map<String, Object> stats = new HashMap<>();

        // 收录应用总数
        Long appCount = webAppMapper.selectCount(null);
        stats.put("appCount", appCount);

        // 总点击次数
        List<WebApp> allApps = webAppMapper.selectList(null);
        long totalClicks = allApps.stream()
                .mapToLong(app -> app.getClickCount() != null ? app.getClickCount() : 0)
                .sum();
        stats.put("totalClicks", totalClicks);

        // 用户总数
        Long userCount = userMapper.selectCount(null);
        stats.put("userCount", userCount);

        // 分类总数
        Long categoryCount = categoryMapper.selectCount(null);
        stats.put("categoryCount", categoryCount);

        // 各分类下的应用数量
        Map<String, Long> categoryStats = new HashMap<>();
        allApps.forEach(app -> {
            String key = app.getCategoryId() != null ? String.valueOf(app.getCategoryId()) : "未分类";
            categoryStats.merge(key, 1L, Long::sum);
        });
        stats.put("categoryStats", categoryStats);

        // 热门应用TOP5（按点击量）
        List<WebApp> topApps = webAppMapper.selectList(
                new LambdaQueryWrapper<WebApp>()
                        .eq(WebApp::getStatus, 1)
                        .orderByDesc(WebApp::getClickCount)
                        .last("LIMIT 5"));
        stats.put("topApps", topApps);

        return stats;
    }
}
