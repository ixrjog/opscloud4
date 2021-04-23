package com.baiyi.opscloud.cloud.slb.builder;

import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersResponse;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlb;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:31 下午
 * @Since 1.0
 */
public class OcAliyunSLBBuilder {

    public static OcAliyunSlb build(DescribeLoadBalancersResponse.LoadBalancer loadBalancer) {
        OcAliyunSlb ocAliyunSlb = new OcAliyunSlb();
        ocAliyunSlb.setLoadBalancerId(loadBalancer.getLoadBalancerId());
        ocAliyunSlb.setLoadBalancerName(loadBalancer.getLoadBalancerName());
        ocAliyunSlb.setLoadBalancerStatus(loadBalancer.getLoadBalancerStatus());
        ocAliyunSlb.setAddress(loadBalancer.getAddress());
        ocAliyunSlb.setAddressType(loadBalancer.getAddressType());
        ocAliyunSlb.setRegionId(loadBalancer.getRegionId());
        ocAliyunSlb.setRegionIdAlias(loadBalancer.getRegionIdAlias());
        ocAliyunSlb.setvSwitchId(loadBalancer.getVSwitchId());
        ocAliyunSlb.setVpcId(loadBalancer.getVpcId());
        ocAliyunSlb.setNetworkType(loadBalancer.getNetworkType());
        ocAliyunSlb.setMasterZoneId(loadBalancer.getMasterZoneId());
        ocAliyunSlb.setSlaveZoneId(loadBalancer.getSlaveZoneId());
        ocAliyunSlb.setInternetChargeType(loadBalancer.getInternetChargeType());
        ocAliyunSlb.setCreateTime(TimeUtils.acqGmtTime(loadBalancer.getCreateTime()));
        ocAliyunSlb.setPayType(loadBalancer.getPayType());
        ocAliyunSlb.setResourceGroupId(loadBalancer.getResourceGroupId());
        ocAliyunSlb.setAddressIpVersion(loadBalancer.getAddressIPVersion());
        ocAliyunSlb.setIsLinkNginx(0);
        return ocAliyunSlb;
    }
}
