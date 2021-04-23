package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcInstance;
import com.baiyi.opscloud.domain.param.opscloud.OpscloudInstanceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcInstanceMapper extends Mapper<OcInstance> {

    List<OcInstance> queryOcInstanceByParam(OpscloudInstanceParam.PageQuery pageQuery);
}