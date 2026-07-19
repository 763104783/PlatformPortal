package com.xx.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * 交付应用实体类（实体量）
 * 
 * 描述一个最终面向终端用户的业务APP，是平台的核心产出物。
 * 对应数据库表：delivery_app
 * 关联关系：
 *   - delivery_app → app_type（多对一，通过 app_type_id 外键）
 *   - delivery_app ←→ biz_process（多对多，通过 app_process_rel 中间表）
 */
@Data
@TableName("delivery_app")
public class DeliveryApp {

    /** 主键ID，自增 */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 应用名称，如"智能报表系统"、"设备监控平台" */
    private String name;

    /** 应用类型ID，关联 app_type 表 */
    private Integer appTypeId;

    /** 应用访问网址链接，点击后在新窗口打开 */
    private String url;

    /** 功能简介，简短描述应用功能 */
    private String description;

    /** 封面图片，存储Base64 Data URL格式，可直接用于<img src>显示 */
    private String coverImage;

    /** 详细介绍，支持较长文本 */
    private String detail;

    /** 版本号，如"1.0.0"、"2.3.1" */
    private String version;

    /** 点击次数，记录用户访问该应用的次数 */
    private Integer clickCount;

    /** 排序号，数值越小越靠前 */
    private Integer sortOrder;

    /** 状态：1=启用（前台可见），0=禁用（前台隐藏） */
    private Integer status;

    /** 创建时间，格式：yyyy-MM-dd HH:mm:ss */
    private String createTime;

    /** 最后更新时间，格式：yyyy-MM-dd HH:mm:ss */
    private String updateTime;

    /**
     * 关联的应用类型对象（非数据库字段）
     * 通过 app_type_id 关联查询获得
     */
    @TableField(exist = false)
    private AppType appType;

    /**
     * 关联的业务流程列表（非数据库字段）
     * 通过 app_process_rel 中间表查询获得
     */
    @TableField(exist = false)
    private List<BizProcess> processes;

    /**
     * 关联的业务流程ID列表（非数据库字段）
     * 用于新增/编辑时接收前端传入的流程ID数组
     */
    @TableField(exist = false)
    private List<Integer> processIds;
}
