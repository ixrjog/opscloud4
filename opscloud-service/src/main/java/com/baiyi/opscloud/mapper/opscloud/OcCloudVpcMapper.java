package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpc;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudVpcMapper extends Mapper<OcCloudVpc> {

    List<OcCloudVpc> fuzzyQueryOcCloudVpcByParam(CloudVPCParam.PageQuery pageQuery);
}