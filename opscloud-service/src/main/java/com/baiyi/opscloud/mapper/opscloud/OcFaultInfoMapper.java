package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcFaultInfo;
import com.baiyi.opscloud.domain.param.fault.FaultParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcFaultInfoMapper extends Mapper<OcFaultInfo> {

    List<OcFaultInfo> queryOcFaultInfoPage(FaultParam.InfoPageQuery pageQuery);
}