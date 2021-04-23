package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcFaultAction;
import com.baiyi.opscloud.domain.param.fault.FaultParam;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

public interface OcFaultActionMapper extends Mapper<OcFaultAction>, InsertListMapper<OcFaultAction> {

    List<OcFaultAction> queryFaultActionPage(FaultParam.ActionPageQuery pageQuery);
}