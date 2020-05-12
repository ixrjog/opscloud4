package com.baiyi.opscloud.service.cloud;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcSecurityGroup;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCSecurityGroupParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/19 1:16 下午
 * @Version 1.0
 */
public interface OcCloudVpcSecurityGroupService {

    DataTable<OcCloudVpcSecurityGroup> queryOcCloudVPCSecurityGroupByParam(CloudVPCSecurityGroupParam.PageQuery pageQuery);

    void deleteOcCloudVpcSecurityGroupByVpcId(String vpcId);

    List<OcCloudVpcSecurityGroup> queryOcCloudVpcSecurityGroupByVpcId(String vpcId);

    OcCloudVpcSecurityGroup queryOcCloudVpcSecurityGroupById(int id);

    OcCloudVpcSecurityGroup queryOcCloudVpcSecurityGroupBySecurityGroupId(String securityGroupId);

    void deleteOcCloudVpcSecurityGroupById(int id);

    void addOcCloudVpcSecurityGroup(OcCloudVpcSecurityGroup ocCloudVpcSecurityGroup);

    void updateOcCloudVpcSecurityGroup(OcCloudVpcSecurityGroup ocCloudVpcSecurityGroup);
}
