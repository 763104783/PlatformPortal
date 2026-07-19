package com.xx.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.platform.entity.DataModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据实体模型Mapper接口
 * 
 * 继承MyBatis-Plus的BaseMapper，自动获得CRUD能力。
 * 对应数据库表：data_model
 */
@Mapper
public interface DataModelMapper extends BaseMapper<DataModel> {
}
