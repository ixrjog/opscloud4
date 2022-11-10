package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import tk.mybatis.mapper.common.Mapper;

public interface LeoBuildMapper extends Mapper<LeoBuild> {

    Integer getMaxBuildNumberWithJobId(Integer jobId);

}