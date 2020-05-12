package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceType;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTypeParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudInstanceTypeMapper extends Mapper<OcCloudInstanceType> {

    List<OcCloudInstanceType> fuzzyQueryOcCloudInstanceTypeByParam(CloudInstanceTypeParam.PageQuery pageQuery);

    List<Integer> queryCpuCoreGroup(@Param("cloudType") int cloudType);
}