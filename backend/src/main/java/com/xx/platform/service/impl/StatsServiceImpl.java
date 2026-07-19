package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xx.platform.entity.AppType;
import com.xx.platform.entity.DeliveryApp;
import com.xx.platform.entity.BizProcess;
import com.xx.platform.entity.DataModel;
import com.xx.platform.entity.AlgoModel;
import com.xx.platform.mapper.*;
import com.xx.platform.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 统计服务实现类
 * 基于新版实体（delivery_app、app_type、biz_process、data_model、algo_model）
 */
@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private DeliveryAppMapper deliveryAppMapper;

    @Autowired
    private AppTypeMapper appTypeMapper;

    @Autowired
    private BizProcessMapper bizProcessMapper;

    @Autowired
    private DataModelMapper dataModelMapper;

    @Autowired
    private AlgoModelMapper algoModelMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public Map<String, Object> getOverviewStats() {
        Map<String, Object> stats = new HashMap<>();

        // 收录应用总数（delivery_app）
        Long appCount = deliveryAppMapper.selectCount(null);
        stats.put("appCount", appCount);

        // 业务流程总数
        Long processCount = bizProcessMapper.selectCount(null);
        stats.put("processCount", processCount);

        // 数据模型总数
        Long dataModelCount = dataModelMapper.selectCount(null);
        stats.put("dataModelCount", dataModelCount);

        // 算法模型总数
        Long algoModelCount = algoModelMapper.selectCount(null);
        stats.put("algoModelCount", algoModelCount);

        // 用户总数
        Long userCount = userMapper.selectCount(null);
        stats.put("userCount", userCount);

        // 总点击次数
        List<DeliveryApp> allApps = deliveryAppMapper.selectList(null);
        long totalClicks = allApps.stream()
                .mapToLong(app -> app.getClickCount() != null ? app.getClickCount() : 0)
                .sum();
        stats.put("totalClicks", totalClicks);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getTopApps(int limit) {
        List<DeliveryApp> topApps = deliveryAppMapper.selectList(
                new LambdaQueryWrapper<DeliveryApp>()
                        .eq(DeliveryApp::getStatus, 1)
                        .orderByDesc(DeliveryApp::getClickCount)
                        .last("LIMIT " + limit));
        List<Map<String, Object>> result = new ArrayList<>();
        for (DeliveryApp app : topApps) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", app.getId());
            item.put("name", app.getName());
            item.put("clickCount", app.getClickCount());
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getAppTypeStats() {
        List<AppType> allTypes = appTypeMapper.selectList(
                new LambdaQueryWrapper<AppType>().orderByAsc(AppType::getSortOrder));
        List<Map<String, Object>> result = new ArrayList<>();
        for (AppType type : allTypes) {
            Long count = deliveryAppMapper.selectCount(
                    new LambdaQueryWrapper<DeliveryApp>()
                            .eq(DeliveryApp::getAppTypeId, type.getId()));
            Map<String, Object> item = new HashMap<>();
            item.put("typeName", type.getName());
            item.put("count", count);
            result.add(item);
        }
        return result;
    }
}
