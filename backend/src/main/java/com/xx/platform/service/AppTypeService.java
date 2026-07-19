package com.xx.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xx.platform.entity.AppType;

import java.util.List;

/**
 * 应用类型服务接口
 * 
 * 提供应用类型（枚举量）的业务操作能力：
 * - 查询所有应用类型列表
 * - 新增应用类型
 * - 编辑应用类型
 * - 删除单个应用类型
 * - 批量删除应用类型
 */
public interface AppTypeService extends IService<AppType> {

    /**
     * 获取所有应用类型列表（按排序号升序）
     * @return 应用类型列表
     */
    List<AppType> listAll();

    /**
     * 批量删除应用类型
     * @param ids 要删除的ID列表
     * @return 是否成功
     */
    boolean batchDelete(List<Integer> ids);
}
