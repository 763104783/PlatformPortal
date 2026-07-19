package com.xx.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 流程-模板关联实体类（中间表）
 * 
 * 维护业务流程与流程模板的多对多关系。
 * 对应数据库表：process_template_rel
 * 
 * 关系说明：
 *   一个业务流程(biz_process)可以基于多个流程模板(process_template)
 *   一个流程模板(process_template)可以被多个业务流程引用
 */
@Data
@TableName("process_template_rel")
public class ProcessTemplateRel {

    /** 主键ID，自增 */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 业务流程ID，关联 biz_process.id */
    private Integer processId;

    /** 流程模板ID，关联 process_template.id */
    private Integer templateId;
}
