package com.xx.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.List;

/**
 * 业务流程实体类（实体量）
 * 
 * 描述一个具体的业务流程实例，可关联多个业务流程模板。
 * 对应数据库表：biz_process
 * 关联关系：biz_process ←→ process_template（多对多，通过 process_template_rel 中间表）
 */
@Data
@TableName("biz_process")
public class BizProcess {

    /** 主键ID，自增 */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 流程名称，如"订单审批流程"、"数据入库流程" */
    private String name;

    /** 流程简介，描述流程的目的和适用场景 */
    private String description;

    /** 制作人，记录流程的创建/维护者 */
    private String author;

    /** 制作时间，格式：yyyy-MM-dd HH:mm:ss */
    private String createTime;

    /** 最后更新时间，格式：yyyy-MM-dd HH:mm:ss */
    private String updateTime;

    /**
     * 关联的流程模板列表（非数据库字段）
     * 通过 process_template_rel 中间表查询获得
     */
    @TableField(exist = false)
    private List<ProcessTemplate> templates;

    /**
     * 关联的流程模板ID列表（非数据库字段）
     * 用于新增/编辑时接收前端传入的模板ID数组
     */
    @TableField(exist = false)
    private List<Integer> templateIds;
}
