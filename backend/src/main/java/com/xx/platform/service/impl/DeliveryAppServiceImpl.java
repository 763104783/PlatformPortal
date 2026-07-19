package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xx.platform.entity.*;
import com.xx.platform.mapper.*;
import com.xx.platform.service.DeliveryAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 交付应用服务实现类
 * 
 * 实现交付应用（业务APP）的完整业务逻辑：
 * - 分页查询（前台/后台两种模式）
 * - 详情查询（填充关联的应用类型和业务流程）
 * - 新增/编辑（维护与业务流程的多对多关联）
 * - 批量删除（同时清理关联关系）
 * - 点击计数
 * 
 * 关联关系：
 * - delivery_app → app_type（多对一）
 * - delivery_app ←→ biz_process（多对多，通过 app_process_rel）
 */
@Service
public class DeliveryAppServiceImpl extends ServiceImpl<DeliveryAppMapper, DeliveryApp> implements DeliveryAppService {

    @Autowired
    private AppProcessRelMapper appProcessRelMapper;

    @Autowired
    private BizProcessMapper bizProcessMapper;

    @Autowired
    private AppTypeMapper appTypeMapper;

    /**
     * 分页查询交付应用（前台展示用）
     * 只查询状态为启用(status=1)的应用
     * 支持关键词搜索、类型筛选、多种排序方式
     */
    @Override
    public IPage<DeliveryApp> pageQuery(int page, int size, String keyword, Integer typeId, String sortField) {
        Page<DeliveryApp> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<DeliveryApp> wrapper = new LambdaQueryWrapper<>();
        
        // 只查询启用状态的应用
        wrapper.eq(DeliveryApp::getStatus, 1);
        
        // 关键词搜索：匹配名称或简介
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                .like(DeliveryApp::getName, keyword)
                .or()
                .like(DeliveryApp::getDescription, keyword)
            );
        }
        
        // 按应用类型筛选
        if (typeId != null) {
            wrapper.eq(DeliveryApp::getAppTypeId, typeId);
        }
        
        // 排序处理
        if ("clickCount".equals(sortField)) {
            wrapper.orderByDesc(DeliveryApp::getClickCount);
        } else if ("name".equals(sortField)) {
            wrapper.orderByAsc(DeliveryApp::getName);
        } else {
            // 默认按排序号升序
            wrapper.orderByAsc(DeliveryApp::getSortOrder);
        }
        
        IPage<DeliveryApp> result = page(pageParam, wrapper);
        
        // 为每条记录填充关联信息
        for (DeliveryApp app : result.getRecords()) {
            fillAppRelations(app);
        }
        
        return result;
    }

    /**
     * 分页查询交付应用（后台管理用）
     * 查询所有状态的应用，供管理员管理
     */
    @Override
    public IPage<DeliveryApp> pageQueryAll(int page, int size, String keyword, Integer typeId) {
        Page<DeliveryApp> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<DeliveryApp> wrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                .like(DeliveryApp::getName, keyword)
                .or()
                .like(DeliveryApp::getDescription, keyword)
            );
        }
        
        // 类型筛选
        if (typeId != null) {
            wrapper.eq(DeliveryApp::getAppTypeId, typeId);
        }
        
        wrapper.orderByAsc(DeliveryApp::getSortOrder);
        IPage<DeliveryApp> result = page(pageParam, wrapper);
        
        // 填充关联信息
        for (DeliveryApp app : result.getRecords()) {
            fillAppRelations(app);
        }
        
        return result;
    }

    /**
     * 获取应用详情（含关联的应用类型和业务流程列表）
     */
    @Override
    public DeliveryApp getDetail(Integer id) {
        DeliveryApp app = getById(id);
        if (app != null) {
            fillAppRelations(app);
        }
        return app;
    }

    /**
     * 新增交付应用
     * 1. 保存应用基本信息
     * 2. 保存应用与业务流程的关联关系
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addApp(DeliveryApp app) {
        // 初始化点击次数为0
        app.setClickCount(0);
        
        // 保存应用基本信息
        boolean saved = save(app);
        
        // 保存流程关联关系
        if (saved && app.getProcessIds() != null && !app.getProcessIds().isEmpty()) {
            saveProcessRelations(app.getId(), app.getProcessIds());
        }
        
        return saved;
    }

    /**
     * 编辑交付应用
     * 1. 更新应用基本信息
     * 2. 删除旧的流程关联，保存新的关联
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateApp(DeliveryApp app) {
        // 更新应用基本信息
        boolean updated = updateById(app);
        
        // 更新流程关联关系：先删后增
        if (updated && app.getProcessIds() != null) {
            deleteProcessRelations(app.getId());
            if (!app.getProcessIds().isEmpty()) {
                saveProcessRelations(app.getId(), app.getProcessIds());
            }
        }
        
        return updated;
    }

    /**
     * 批量删除交付应用
     * 同时删除相关的流程关联关系
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return false;
        
        // 删除每个应用的流程关联
        for (Integer id : ids) {
            deleteProcessRelations(id);
        }
        
        // 删除应用本身
        return removeByIds(ids);
    }

    /**
     * 记录应用点击次数
     * 使用SQL自增方式，避免并发问题
     */
    @Override
    public void incrementClickCount(Integer id) {
        LambdaUpdateWrapper<DeliveryApp> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(DeliveryApp::getId, id)
               .setSql("click_count = click_count + 1");
        update(wrapper);
    }

    /**
     * 填充应用的关联信息
     * 包括：应用类型对象、业务流程列表
     */
    private void fillAppRelations(DeliveryApp app) {
        // 填充应用类型
        if (app.getAppTypeId() != null) {
            AppType appType = appTypeMapper.selectById(app.getAppTypeId());
            app.setAppType(appType);
        }
        
        // 填充业务流程列表
        app.setProcesses(getProcessesByAppId(app.getId()));
    }

    /**
     * 根据应用ID获取关联的业务流程列表
     */
    private List<BizProcess> getProcessesByAppId(Integer appId) {
        // 查询关联关系
        LambdaQueryWrapper<AppProcessRel> relWrapper = new LambdaQueryWrapper<>();
        relWrapper.eq(AppProcessRel::getAppId, appId);
        List<AppProcessRel> relations = appProcessRelMapper.selectList(relWrapper);
        
        if (relations.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取流程ID列表
        List<Integer> processIds = relations.stream()
            .map(AppProcessRel::getProcessId)
            .collect(Collectors.toList());
        
        // 查询流程详情
        return bizProcessMapper.selectBatchIds(processIds);
    }

    /**
     * 保存应用与业务流程的关联关系
     */
    private void saveProcessRelations(Integer appId, List<Integer> processIds) {
        for (Integer processId : processIds) {
            AppProcessRel rel = new AppProcessRel();
            rel.setAppId(appId);
            rel.setProcessId(processId);
            appProcessRelMapper.insert(rel);
        }
    }

    /**
     * 删除应用的所有流程关联关系
     */
    private void deleteProcessRelations(Integer appId) {
        LambdaQueryWrapper<AppProcessRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AppProcessRel::getAppId, appId);
        appProcessRelMapper.delete(wrapper);
    }
}
