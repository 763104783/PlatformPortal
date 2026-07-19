package com.xx.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 应用-流程关联实体类（中间表）
 * 
 * 维护交付应用与业务流程的多对多关系。
 * 对应数据库表：app_process_rel
 * 
 * 关系说明：
 *   一个交付应用(delivery_app)可以关联多个业务流程(biz_process)
 *   一个业务流程(biz_process)可以被多个交付应用引用
 */
@Data
@TableName("app_process_rel")
public class AppProcessRel {

    /** 主键ID，自增 */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 交付应用ID，关联 delivery_app.id */
    private Integer appId;

    /** 业务流程ID，关联 biz_process.id */
    private Integer processId;
}
