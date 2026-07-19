package com.xx.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.platform.entity.ProcessTemplateRel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 流程-模板关联Mapper接口
 * 
 * 继承MyBatis-Plus的BaseMapper，自动获得CRUD能力。
 * 对应数据库表：process_template_rel
 */
@Mapper
public interface ProcessTemplateRelMapper extends BaseMapper<ProcessTemplateRel> {
}
