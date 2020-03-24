package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.CloudInstanceTemplateUtils;
import com.baiyi.opscloud.domain.generator.OcCloudInstanceTemplate;
import com.baiyi.opscloud.domain.generator.OcCloudInstanceType;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTemplateVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTypeVO;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTypeService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


/**
 * @Author baiyi
 * @Date 2020/3/23 7:09 下午
 * @Version 1.0
 */
@Component
public class CloudInstanceTemplateDecorator {

    @Resource
    private CloudInstanceTypeDecorator cloudInstanceTypeDecorator;

    @Resource
    private OcCloudInstanceTypeService ocCloudInstanceTypeService;


    public OcCloudInstanceTemplateVO.CloudInstanceTemplate decorator(OcCloudInstanceTemplate ocCloudInstanceTemplate) {
        OcCloudInstanceTemplateVO.CloudInstanceTemplate instanceTemplate
                = BeanCopierUtils.copyProperties(ocCloudInstanceTemplate, OcCloudInstanceTemplateVO.CloudInstanceTemplate.class);

        // 序列化YAML
        if (!StringUtils.isEmpty(ocCloudInstanceTemplate.getTemplateYaml())) {
            instanceTemplate.setInstanceTemplate(CloudInstanceTemplateUtils.convert(ocCloudInstanceTemplate.getTemplateYaml()));
            instanceTemplate.setTemplateYAML(ocCloudInstanceTemplate.getTemplateYaml());
        }
        return decorator(instanceTemplate);
    }


    public OcCloudInstanceTemplateVO.CloudInstanceTemplate decorator(OcCloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {

        // 序列化YAML
        if (!StringUtils.isEmpty(cloudInstanceTemplate.getTemplateYAML()))
            cloudInstanceTemplate.setInstanceTemplate(CloudInstanceTemplateUtils.convert(cloudInstanceTemplate.getTemplateYAML()));


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


    public OcCloudInstanceTemplateVO.InstanceTemplate decorator(OcCloudInstanceTemplateVO.InstanceTemplate instanceTemplate) {
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
                instanceTemplate.setZoneIds(Lists.newArrayList(cloudInstanceType.getZones()));
            }
        }
        return instanceTemplate;
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
