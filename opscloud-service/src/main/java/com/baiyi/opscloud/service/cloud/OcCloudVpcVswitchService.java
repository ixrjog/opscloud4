package com.baiyi.opscloud.service.cloud;

import com.baiyi.opscloud.domain.generator.OcCloudVpcVswitch;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/19 9:57 上午
 * @Version 1.0
 */
public interface OcCloudVpcVswitchService {

    void deleteOcCloudVpcVswitchByVpcId(String vpcId);

    List<OcCloudVpcVswitch> queryOcCloudVpcVswitchByVpcId(String vpcId);

    OcCloudVpcVswitch queryOcCloudVpcVswitchById(int id);

    OcCloudVpcVswitch queryOcCloudVpcVswitchByVswitchId(String vswitchId);

    void deleteOcCloudVpcVswitchById(int id);

    void addOcCloudVpcVswitch(OcCloudVpcVswitch ocCloudVpcVswitch);
}
