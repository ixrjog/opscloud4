package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.OcCloudVpcSecurityGroup;
import com.baiyi.opscloud.domain.generator.OcCloudVpcVswitch;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudVPCSecurityGroupVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudVPCVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudVSwitchVO;
import com.baiyi.opscloud.service.cloud.OcCloudVpcSecurityGroupService;
import com.baiyi.opscloud.service.cloud.OcCloudVpcVswitchService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/3/19 2:49 下午
 * @Version 1.0
 */
@Component
public class CloudVPCDecorator {

    @Resource
    private OcCloudVpcSecurityGroupService ocCloudVpcSecurityGroupService;

    @Resource
    private OcCloudVpcVswitchService ocCloudVpcVswitchService;

    public OcCloudVPCVO.CloudVpc decorator(OcCloudVPCVO.CloudVpc cloudVpc, Integer extend) {
        if (extend != null && extend == 1) {
            String vpcId = cloudVpc.getVpcId();
            // 装饰安全组
            List<OcCloudVpcSecurityGroup> securityGroupList = ocCloudVpcSecurityGroupService.queryOcCloudVpcSecurityGroupByVpcId(vpcId);
            cloudVpc.setSecurityGroups(BeanCopierUtils.copyListProperties(securityGroupList, OcCloudVPCSecurityGroupVO.SecurityGroup.class));
            // 装饰虚拟交换机 by zoneId
            List<OcCloudVpcVswitch> vswitchList = ocCloudVpcVswitchService.queryOcCloudVpcVswitchByVpcId(vpcId);
            cloudVpc.setVswitchMap(getVswitchMap(vswitchList));
        }
        return cloudVpc;
    }

    /**
     * 模版设置专用（按zoneIds匹配）
     * @param cloudVpc
     * @param zoneIds
     * @return
     */
    public OcCloudVPCVO.CloudVpc decorator(OcCloudVPCVO.CloudVpc cloudVpc, List<String> zoneIds) {
        String vpcId = cloudVpc.getVpcId();
        // 装饰安全组
        List<OcCloudVpcSecurityGroup> securityGroupList = ocCloudVpcSecurityGroupService.queryOcCloudVpcSecurityGroupByVpcId(vpcId);
        cloudVpc.setSecurityGroups(BeanCopierUtils.copyListProperties(securityGroupList, OcCloudVPCSecurityGroupVO.SecurityGroup.class));
        // 装饰虚拟交换机 by zoneId
        List<OcCloudVpcVswitch> vswitchList = ocCloudVpcVswitchService.queryOcCloudVpcVswitchByVpcIdAndZoneIds(vpcId, zoneIds);
        cloudVpc.setVswitchMap(getVswitchMap(vswitchList));
        return cloudVpc;
    }

    private Map<String, List<OcCloudVSwitchVO.VSwitch>> getVswitchMap(List<OcCloudVpcVswitch> vswitchList) {
        // zoneId
        Map<String, List<OcCloudVSwitchVO.VSwitch>> map = Maps.newHashMap();
        for (OcCloudVpcVswitch vswitch : vswitchList) {
            OcCloudVSwitchVO.VSwitch vsw = BeanCopierUtils.copyProperties(vswitch, OcCloudVSwitchVO.VSwitch.class);
            if (map.containsKey(vswitch.getZoneId())) {
                map.get(vswitch.getZoneId()).add(vsw);
            } else {
                List<OcCloudVSwitchVO.VSwitch> list = Lists.newArrayList();
                list.add(vsw);
                map.put(vswitch.getZoneId(), list);
            }
        }
        return map;
    }
}
