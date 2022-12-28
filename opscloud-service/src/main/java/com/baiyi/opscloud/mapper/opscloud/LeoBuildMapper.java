package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface LeoBuildMapper extends Mapper<LeoBuild> {

    Integer getMaxBuildNumberWithJobId(Integer jobId);

    List<LeoBuild> queryPageByParam(LeoJobParam.JobBuildPageQuery pageQuery);

}