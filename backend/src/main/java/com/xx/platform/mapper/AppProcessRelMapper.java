package com.xx.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.platform.entity.AppProcessRel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用-流程关联Mapper接口
 * 
 * 继承MyBatis-Plus的BaseMapper，自动获得CRUD能力。
 * 对应数据库表：app_process_rel
 */
@Mapper
public interface AppProcessRelMapper extends BaseMapper<AppProcessRel> {
}
