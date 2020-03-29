package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.OcCloudInstanceType;
import com.baiyi.opscloud.domain.generator.OcCloudVpc;
import com.baiyi.opscloud.domain.generator.OcCloudVpcSecurityGroup;
import com.baiyi.opscloud.domain.generator.OcCloudVpcVswitch;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTemplateVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTypeVO;
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


    private OcCloudInstanceTemplateVO.InstanceTemplate builder(OcCloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {
        OcCloudInstanceTemplateVO.InstanceTemplate instanceTemplate = new OcCloudInstanceTemplateVO.InstanceTemplate();
        instanceTemplate.setCloudType(cloudInstanceTemplate.getCloudType());
        if (!StringUtils.isEmpty(cloudInstanceTemplate.getRegionId()))
            instanceTemplate.setRegionId(cloudInstanceTemplate.getRegionId());
        return instanceTemplate;
    }

    public OcCloudInstanceTemplateVO.InstanceTemplate decorator(OcCloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {
        OcCloudInstanceTemplateVO.InstanceTemplate instanceTemplate = cloudInstanceTemplate.getInstanceTemplate();
        if (instanceTemplate == null)
            return builder(cloudInstanceTemplate);

        instanceTemplate.setRegionId(cloudInstanceTemplate.getRegionId());
        // 重新组装
        if (instanceTemplate.getInstance() != null && !StringUtils.isEmpty(instanceTemplate.getInstance().getTypeId())) {
            OcCloudInstanceTypeVO.CloudInstanceType cloudInstanceType = getCloudInstanceType(instanceTemplate);
            if (cloudInstanceType != null) {
                OcCloudInstanceTemplateVO.Instance instance = new OcCloudInstanceTemplateVO.Instance();
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
        List<OcCloudInstanceTemplateVO.VSwitch> vswitchs = getVswitchs(cloudInstanceTemplate.getVswitchChecked());
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

    private List<OcCloudInstanceTemplateVO.VSwitch> getVswitchs(List<String> vswitchChecked) {
        List<OcCloudInstanceTemplateVO.VSwitch> vswitchs = Lists.newArrayList();
        if (vswitchChecked == null) return vswitchs;
        for (String vswitchId : vswitchChecked) {
            if (vswitchId == null) continue;
            OcCloudVpcVswitch ocCloudVpcVswitch = ocCloudVpcVswitchService.queryOcCloudVpcVswitchByVswitchId(vswitchId);
            if (ocCloudVpcVswitch == null) continue;
            OcCloudInstanceTemplateVO.VSwitch vSwitch = new OcCloudInstanceTemplateVO.VSwitch();
            vSwitch.setVswitchId(ocCloudVpcVswitch.getVswitchId());
            vSwitch.setVswitchName(ocCloudVpcVswitch.getVswitchName());
            vSwitch.setZoneId(ocCloudVpcVswitch.getZoneId());
            vswitchs.add(vSwitch);
        }
        return vswitchs;
    }

    private OcCloudInstanceTypeVO.CloudInstanceType getCloudInstanceType(OcCloudInstanceTemplateVO.InstanceTemplate instanceTemplate) {
        OcCloudInstanceType ocCloudInstanceType = new OcCloudInstanceType();
        ocCloudInstanceType.setCloudType(instanceTemplate.getCloudType());
        ocCloudInstanceType.setInstanceTypeId(instanceTemplate.getInstance().getTypeId());
        ocCloudInstanceType = ocCloudInstanceTypeService.queryOcCloudInstanceByUniqueKey(ocCloudInstanceType);
        if (ocCloudInstanceType == null)
            return new OcCloudInstanceTypeVO.CloudInstanceType();
        OcCloudInstanceTypeVO.CloudInstanceType cloudInstanceType = BeanCopierUtils.copyProperties(ocCloudInstanceType, OcCloudInstanceTypeVO.CloudInstanceType.class);
        return cloudInstanceTypeDecorator.decorator(cloudInstanceType, instanceTemplate.getRegionId(), 1);
    }
}
