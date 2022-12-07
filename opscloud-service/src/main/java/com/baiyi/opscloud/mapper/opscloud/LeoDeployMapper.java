package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import tk.mybatis.mapper.common.Mapper;

public interface LeoDeployMapper extends Mapper<LeoDeploy> {

    Integer getMaxDeployNumberWithJobId(Integer jobId);

}