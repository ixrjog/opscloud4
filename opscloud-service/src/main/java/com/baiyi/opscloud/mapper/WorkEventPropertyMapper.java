package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.WorkEventProperty;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface WorkEventPropertyMapper extends Mapper<WorkEventProperty>, InsertListMapper<WorkEventProperty> {
}