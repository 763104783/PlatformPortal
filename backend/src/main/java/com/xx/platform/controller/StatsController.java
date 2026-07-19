package com.xx.platform.controller;

import com.xx.platform.common.Result;
import com.xx.platform.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 统计控制器
 * 提供平台统计数据
 */
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    /**
     * 获取平台总览统计
     * GET /api/stats/overview
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        return Result.success(statsService.getOverviewStats());
    }

    /**
     * 获取应用点击排行
     * GET /api/stats/top-apps?limit=5
     */
    @GetMapping("/top-apps")
    public Result<List<Map<String, Object>>> topApps(
            @RequestParam(defaultValue = "5") int limit) {
        return Result.success(statsService.getTopApps(limit));
    }

    /**
     * 获取各应用类型下的应用数量统计
     * GET /api/stats/app-type-stats
     */
    @GetMapping("/app-type-stats")
    public Result<List<Map<String, Object>>> appTypeStats() {
        return Result.success(statsService.getAppTypeStats());
    }
}
