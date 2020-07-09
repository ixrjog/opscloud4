package com.baiyi.opscloud.decorator.cloud;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceType;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpc;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcSecurityGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcVswitch;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTemplateVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTypeVO;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTypeService;
import com.baiyi.opscloud.service.cloud.OcCloudVpcSecurityGroupService;
import com.baiyi.opscloud.service.cloud.OcCloudVpcService;
import com.baiyi.opscloud.service.cloud.OcCloudVpcVswitchService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/25 9:55 上午
 * @Version 1.0
 */
@Component
public class InstanceTemplateDecorator {

    @Resource
    private CloudInstanceTypeDecorator cloudInstanceTypeDecorator;

    @Resource
    private OcCloudInstanceTypeService ocCloudInstanceTypeService;

    @Resource
    private OcCloudVpcVswitchService ocCloudVpcVswitchService;

    @Resource
    private OcCloudVpcSecurityGroupService ocCloudVpcSecurityGroupService;

    @Resource
    private OcCloudVpcService ocCloudVpcService;


    private CloudInstanceTemplateVO.InstanceTemplate builder(CloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {
        CloudInstanceTemplateVO.InstanceTemplate instanceTemplate = new CloudInstanceTemplateVO.InstanceTemplate();
        instanceTemplate.setCloudType(cloudInstanceTemplate.getCloudType());
        if (!StringUtils.isEmpty(cloudInstanceTemplate.getRegionId()))
            instanceTemplate.setRegionId(cloudInstanceTemplate.getRegionId());
        return instanceTemplate;
    }

    public CloudInstanceTemplateVO.InstanceTemplate decorator(CloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {
        CloudInstanceTemplateVO.InstanceTemplate instanceTemplate = cloudInstanceTemplate.getInstanceTemplate();
        if (instanceTemplate == null)
            return builder(cloudInstanceTemplate);

        instanceTemplate.setRegionId(cloudInstanceTemplate.getRegionId());
        // 重新组装
        if (instanceTemplate.getInstance() != null && !StringUtils.isEmpty(instanceTemplate.getInstance().getTypeId())) {
            CloudInstanceTypeVO.CloudInstanceType cloudInstanceType = getCloudInstanceType(instanceTemplate);
            if (cloudInstanceType != null) {
                CloudInstanceTemplateVO.Instance instance = new CloudInstanceTemplateVO.Instance();
                instance.setCpuCoreCount(cloudInstanceType.getCpuCoreCount());
                instance.setMemorySize(cloudInstanceType.getMemorySize());
                instance.setTypeFamily(cloudInstanceType.getInstanceTypeFamily());
                instance.setTypeId(cloudInstanceType.getInstanceTypeId());
                instanceTemplate.setInstance(instance);
                if (cloudInstanceType.getZones() != null && !cloudInstanceType.getZones().isEmpty())
                    instanceTemplate.setZoneIds(Lists.newArrayList(cloudInstanceType.getZones()));
            }
        }
        // 组装虚拟交换机
        List<CloudInstanceTemplateVO.VSwitch> vswitchs = getVswitchs(cloudInstanceTemplate.getVswitchChecked());
        if (!vswitchs.isEmpty())
            instanceTemplate.setVswitchs(getVswitchs(cloudInstanceTemplate.getVswitchChecked()));
        // 组装vpc
        if (!StringUtils.isEmpty(cloudInstanceTemplate.getVpcId())) {
            OcCloudVpc ocCloudVpc = ocCloudVpcService.queryOcCloudVpcByVpcId(cloudInstanceTemplate.getVpcId());
            if (ocCloudVpc != null) {
                instanceTemplate.setVpcId(ocCloudVpc.getVpcId());
                instanceTemplate.setVpcName(ocCloudVpc.getVpcName());
            }
        }
        // 组装vpc安全组
        if (!StringUtils.isEmpty(cloudInstanceTemplate.getSecurityGroupId())) {
            OcCloudVpcSecurityGroup ocCloudVpcSecurityGroup
                    = ocCloudVpcSecurityGroupService.queryOcCloudVpcSecurityGroupBySecurityGroupId(cloudInstanceTemplate.getSecurityGroupId());
            if (ocCloudVpcSecurityGroup != null) {
                instanceTemplate.setSecurityGroupId(ocCloudVpcSecurityGroup.getSecurityGroupId());
                instanceTemplate.setSecurityGroupName(ocCloudVpcSecurityGroup.getSecurityGroupName());
            }
        }
        return instanceTemplate;
    }

    private List<CloudInstanceTemplateVO.VSwitch> getVswitchs(List<String> vswitchChecked) {
        List<CloudInstanceTemplateVO.VSwitch> vswitchs = Lists.newArrayList();
        if (vswitchChecked == null) return vswitchs;
        for (String vswitchId : vswitchChecked) {
            if (vswitchId == null) continue;
            OcCloudVpcVswitch ocCloudVpcVswitch = ocCloudVpcVswitchService.queryOcCloudVpcVswitchByVswitchId(vswitchId);
            if (ocCloudVpcVswitch == null) continue;
            CloudInstanceTemplateVO.VSwitch vSwitch = new CloudInstanceTemplateVO.VSwitch();
            vSwitch.setVswitchId(ocCloudVpcVswitch.getVswitchId());
            vSwitch.setVswitchName(ocCloudVpcVswitch.getVswitchName());
            vSwitch.setZoneId(ocCloudVpcVswitch.getZoneId());
            vswitchs.add(vSwitch);
        }
        return vswitchs;
    }

    private CloudInstanceTypeVO.CloudInstanceType getCloudInstanceType(CloudInstanceTemplateVO.InstanceTemplate instanceTemplate) {
        OcCloudInstanceType ocCloudInstanceType = new OcCloudInstanceType();
        ocCloudInstanceType.setCloudType(instanceTemplate.getCloudType());
        ocCloudInstanceType.setInstanceTypeId(instanceTemplate.getInstance().getTypeId());
        ocCloudInstanceType = ocCloudInstanceTypeService.queryOcCloudInstanceByUniqueKey(ocCloudInstanceType);
        if (ocCloudInstanceType == null)
            return new CloudInstanceTypeVO.CloudInstanceType();
        CloudInstanceTypeVO.CloudInstanceType cloudInstanceType = BeanCopierUtils.copyProperties(ocCloudInstanceType, CloudInstanceTypeVO.CloudInstanceType.class);
        return cloudInstanceTypeDecorator.decorator(cloudInstanceType, instanceTemplate.getRegionId(), 1);
    }
}
