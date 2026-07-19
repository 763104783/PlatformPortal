package com.xx.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.platform.entity.AppType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用类型Mapper接口
 * 
 * 继承MyBatis-Plus的BaseMapper，自动获得以下CRUD能力：
 * - insert：新增应用类型
 * - deleteById：根据ID删除
 * - updateById：根据ID更新
 * - selectById：根据ID查询
 * - selectList：条件查询列表
 * - selectPage：分页查询
 * 
 * 对应数据库表：app_type
 */
@Mapper
public interface AppTypeMapper extends BaseMapper<AppType> {
}
