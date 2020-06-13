package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.param.env.EnvParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcEnvMapper extends Mapper<OcEnv> {

    List<OcEnv> queryOcEnvByParam(EnvParam.PageQuery pageQuery);
}