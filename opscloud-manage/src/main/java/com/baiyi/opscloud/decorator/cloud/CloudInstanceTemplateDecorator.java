package com.baiyi.opscloud.decorator.cloud;

import com.baiyi.opscloud.aliyun.ecs.AliyunInstance;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.CloudInstanceTemplateUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudImage;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudInstanceTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcSecurityGroup;
import com.baiyi.opscloud.domain.vo.cloud.CloudImageVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTemplateVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudVPCSecurityGroupVO;
import com.baiyi.opscloud.service.cloud.OcCloudImageService;
import com.baiyi.opscloud.service.cloud.OcCloudVpcSecurityGroupService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @Author baiyi
 * @Date 2020/3/23 7:09 下午
 * @Version 1.0
 */
@Component
public class CloudInstanceTemplateDecorator {

    @Resource
    private AliyunInstance aliyunInstance;

    @Resource
    private OcCloudVpcSecurityGroupService ocCloudVpcSecurityGroupService;

    @Resource
    private OcCloudImageService ocCloudImageService;

    public CloudInstanceTemplateVO.CloudInstanceTemplate decorator(OcCloudInstanceTemplate ocCloudInstanceTemplate) {
        CloudInstanceTemplateVO.CloudInstanceTemplate instanceTemplate
                = BeanCopierUtils.copyProperties(ocCloudInstanceTemplate, CloudInstanceTemplateVO.CloudInstanceTemplate.class);
        // 序列化YAML
        if (!StringUtils.isEmpty(ocCloudInstanceTemplate.getTemplateYaml())) {
            instanceTemplate.setInstanceTemplate(CloudInstanceTemplateUtils.convert(ocCloudInstanceTemplate.getTemplateYaml()));
            instanceTemplate.setTemplateYAML(ocCloudInstanceTemplate.getTemplateYaml());
        }
        return decorator(instanceTemplate);
    }

    /**
     * 转换 VPC虚拟交换机为Map
     *
     * @param instanceTemplate
     * @return
     */
    protected static Map<String, CloudInstanceTemplateVO.VSwitch> getVSwitchZoneIdMap(CloudInstanceTemplateVO.InstanceTemplate instanceTemplate) {
        if (instanceTemplate.getVswitchs() == null)
            return Maps.newHashMap();
        List<CloudInstanceTemplateVO.VSwitch> vswitchs = instanceTemplate.getVswitchs();
        return vswitchs.stream().collect(Collectors.toMap(CloudInstanceTemplateVO.VSwitch::getZoneId, a -> a, (k1, k2) -> k1));
    }

    private List<CloudInstanceTemplateVO.InstanceZone> getInstanceZones(String regionId, CloudInstanceTemplateVO.Instance instance, Map<String, CloudInstanceTemplateVO.VSwitch> vswitchZoneIdMap) {
        List<CloudInstanceTemplateVO.InstanceZone> instanceZones = Lists.newArrayList();
        if (StringUtils.isEmpty(instance.getTypeId())) return instanceZones;
        Map<String, Set<String>> instanceTypeZoneMap = aliyunInstance.getInstanceTypeZoneMap(regionId);
        if (!instanceTypeZoneMap.containsKey(instance.getTypeId())) return instanceZones;
        Set<String> zoneIds = instanceTypeZoneMap.get(instance.getTypeId());
        for (String zoneId : zoneIds) {
            CloudInstanceTemplateVO.InstanceZone instanceZone = new CloudInstanceTemplateVO.InstanceZone();
            instanceZone.setZoneId(zoneId);
            if (vswitchZoneIdMap.containsKey(zoneId)) {
                instanceZone.setActive(true);
            }
            instanceZones.add(instanceZone);
        }
        return instanceZones;
    }


    public CloudInstanceTemplateVO.CloudInstanceTemplate decorator(CloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {
        // 序列化YAML
        if (!StringUtils.isEmpty(cloudInstanceTemplate.getTemplateYAML())) {
            CloudInstanceTemplateVO.InstanceTemplate instanceTemplate = CloudInstanceTemplateUtils.convert(cloudInstanceTemplate.getTemplateYAML());
            cloudInstanceTemplate.setInstanceTemplate(instanceTemplate);
            // 装饰实例可用区
            if (instanceTemplate.getInstance() != null) {
                Map<String, CloudInstanceTemplateVO.VSwitch> vswitchZoneIdMap = getVSwitchZoneIdMap(cloudInstanceTemplate.getInstanceTemplate());
                cloudInstanceTemplate.setInstanceZones(getInstanceZones(cloudInstanceTemplate.getRegionId(), instanceTemplate.getInstance(), vswitchZoneIdMap));
            }
        }
        // 装饰 安全组
        if (!StringUtils.isEmpty(cloudInstanceTemplate.getSecurityGroupId())) {
            OcCloudVpcSecurityGroup ocCloudVpcSecurityGroup = ocCloudVpcSecurityGroupService.queryOcCloudVpcSecurityGroupBySecurityGroupId(cloudInstanceTemplate.getSecurityGroupId());
            if (ocCloudVpcSecurityGroup != null)
                cloudInstanceTemplate.setSecurityGroup(BeanCopierUtils.copyProperties(ocCloudVpcSecurityGroup, CloudVPCSecurityGroupVO.SecurityGroup.class));
        }
        // 装饰 云镜像
        if (!StringUtils.isEmpty(cloudInstanceTemplate.getImageId())) {
            OcCloudImage ocCloudImage = ocCloudImageService.queryOcCloudImageByImageId(cloudInstanceTemplate.getImageId());
            if (ocCloudImage != null)
                cloudInstanceTemplate.setCloudImage(BeanCopierUtils.copyProperties(ocCloudImage, CloudImageVO.CloudImage.class));
        }

//        Yaml yaml = new Yaml();
//        ServerAttributeBO serverAttributeBO = ServerAttributeBO.builder()
//                .businessId(businessId)
//                .businessType(businessType)
//                .groupName(attributeGroup.getName())
//                .comment(attributeGroup.getComment())
//                .attributes(yaml.dumpAs(attributeGroup, Tag.MAP, DumperOptions.FlowStyle.BLOCK)) // 序列化对象为YAML-BLOCK格式
//                .build();

        return cloudInstanceTemplate;
    }


}
