package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderGroup;
import com.baiyi.opscloud.domain.param.workorder.WorkorderGroupParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcWorkorderGroupMapper extends Mapper<OcWorkorderGroup> {

    List<OcWorkorderGroup> queryOcWorkorderGroupByParam(WorkorderGroupParam.PageQuery pageQuery);
}