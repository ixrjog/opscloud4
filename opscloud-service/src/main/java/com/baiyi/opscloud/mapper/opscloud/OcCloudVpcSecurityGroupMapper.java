package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcSecurityGroup;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCSecurityGroupParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcCloudVpcSecurityGroupMapper extends Mapper<OcCloudVpcSecurityGroup> {

    List<OcCloudVpcSecurityGroup> queryOcCloudVPCSecurityGroupByParam(CloudVPCSecurityGroupParam.PageQuery pageQuery);
}