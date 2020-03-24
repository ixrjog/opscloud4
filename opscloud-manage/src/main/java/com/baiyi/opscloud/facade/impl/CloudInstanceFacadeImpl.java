package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.ecs.AliyunInstance;
import com.baiyi.opscloud.aliyun.ecs.base.AliyunInstanceTypeVO;
import com.baiyi.opscloud.builder.CloudInstanceTemplateBuilder;
import com.baiyi.opscloud.builder.CloudInstanceTypeBuilder;
import com.baiyi.opscloud.common.base.CloudType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.CloudInstanceTemplateUtils;
import com.baiyi.opscloud.decorator.CloudInstanceTemplateDecorator;
import com.baiyi.opscloud.decorator.CloudInstanceTypeDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.OcCloudInstanceTemplate;
import com.baiyi.opscloud.domain.generator.OcCloudInstanceType;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTemplateParam;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTypeParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTemplateVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudInstanceTypeVO;
import com.baiyi.opscloud.facade.CloudInstanceFacade;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTemplateService;
import com.baiyi.opscloud.service.cloud.OcCloudInstanceTypeService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:42 下午
 * @Version 1.0
 */
@Service
public class CloudInstanceFacadeImpl implements CloudInstanceFacade {

    @Resource
    private OcCloudInstanceTemplateService ocCloudInstanceTemplateService;

    @Resource
    private OcCloudInstanceTypeService ocCloudInstanceTypeService;

    @Resource
    private CloudInstanceTypeDecorator cloudInstanceTypeDecorator;

    @Resource
    private CloudInstanceTemplateDecorator cloudInstanceTemplateDecorator;

    @Resource
    private AliyunInstance aliyunInstance;

    @Resource
    private AliyunCore aliyunCore;

    @Override
    public DataTable<OcCloudInstanceTemplateVO.CloudInstanceTemplate> fuzzyQueryCloudInstanceTemplatePage(CloudInstanceTemplateParam.PageQuery pageQuery) {
        DataTable<OcCloudInstanceTemplate> table = ocCloudInstanceTemplateService.fuzzyQueryOcCloudInstanceTemplateByParam(pageQuery);
        DataTable<OcCloudInstanceTemplateVO.CloudInstanceTemplate> dataTable
                = new DataTable<>(table.getData().stream().map(e -> cloudInstanceTemplateDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> saveCloudInstanceTemplate(OcCloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {
        OcCloudInstanceTemplate pre = BeanCopierUtils.copyProperties(cloudInstanceTemplate, OcCloudInstanceTemplate.class);
        OcCloudInstanceTemplateVO.InstanceTemplate instanceTemplate = cloudInstanceTemplate.getInstanceTemplate();
        // 重新组装
        instanceTemplate = cloudInstanceTemplateDecorator.decorator(instanceTemplate);
        Yaml yaml = new Yaml();
        pre.setTemplateYaml(yaml.dumpAs(instanceTemplate, Tag.MAP, DumperOptions.FlowStyle.BLOCK));// 序列化对象为YAML-BLOCK格式
        if (pre.getCloudType() == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_TYPE_IS_NULL);
        if (StringUtils.isEmpty(cloudInstanceTemplate.getTemplateName()))
            return new BusinessWrapper<>(ErrorEnum.CLOUD_INSTANCE_TEMPLATE_NAME_NON_COMPLIANCE_WITH_RULES);

        if (pre.getId() == null || pre.getId() <= 0) {
            ocCloudInstanceTemplateService.addOcCloudInstanceTemplate(pre);
        } else {
            ocCloudInstanceTemplateService.updateOcCloudInstanceTemplate(pre);
        }
        BusinessWrapper wrapper = BusinessWrapper.SUCCESS;
        wrapper.setBody(cloudInstanceTemplateDecorator.decorator(pre));
        return wrapper;
    }

    @Override
    public BusinessWrapper<Boolean> saveCloudInstanceTemplateYAML(OcCloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {
        // YAML对象
        OcCloudInstanceTemplateVO.InstanceTemplate instanceTemplate = CloudInstanceTemplateUtils.convert(cloudInstanceTemplate.getTemplateYAML());
        // 通过YAML对象组装模版
        OcCloudInstanceTemplate pre = CloudInstanceTemplateBuilder.build(instanceTemplate,cloudInstanceTemplate.getId());
        if (pre.getCloudType() == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_TYPE_IS_NULL);
        if (StringUtils.isEmpty(cloudInstanceTemplate.getTemplateName()))
            return new BusinessWrapper<>(ErrorEnum.CLOUD_INSTANCE_TEMPLATE_NAME_NON_COMPLIANCE_WITH_RULES);
        if (pre.getId() == null || pre.getId() <= 0) {
            ocCloudInstanceTemplateService.addOcCloudInstanceTemplate(pre);
        } else {
            ocCloudInstanceTemplateService.updateOcCloudInstanceTemplate(pre);
        }
        BusinessWrapper wrapper = BusinessWrapper.SUCCESS;
        wrapper.setBody(cloudInstanceTemplateDecorator.decorator(pre));
        return wrapper;
    }

    @Override
    public DataTable<OcCloudInstanceTypeVO.CloudInstanceType> fuzzyQueryCloudInstanceTypePage(CloudInstanceTypeParam.PageQuery pageQuery) {
        DataTable<OcCloudInstanceType> table = ocCloudInstanceTypeService.fuzzyQueryOcCloudInstanceTypeByParam(pageQuery);
        List<OcCloudInstanceTypeVO.CloudInstanceType> page = BeanCopierUtils.copyListProperties(table.getData(), OcCloudInstanceTypeVO.CloudInstanceType.class);
        DataTable<OcCloudInstanceTypeVO.CloudInstanceType> dataTable
                = new DataTable<>(page.stream().map(e -> cloudInstanceTypeDecorator.decorator(e, pageQuery.getRegionId(), pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<Boolean> deleteCloudInstanceTemplateById(int id) {
        if (ocCloudInstanceTemplateService.queryOcCloudInstanceTemplateById(id) == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_INSTANCE_TEMPLATE_NOT_EXIST);
        ocCloudInstanceTemplateService.deleteOcCloudInstanceTemplateById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> syncInstanceType(int cloudType) {
        Map<String, OcCloudInstanceType> typeMap = getInstanceTypeMap(ocCloudInstanceTypeService.queryOcCloudInstanceTypeByType(CloudType.ALIYUN.getType()));

        if (cloudType == CloudType.ALIYUN.getType()) {
            Map<String, AliyunInstanceTypeVO.InstanceType> map = aliyunInstance.getInstanceTypeMap();
            for (String instanceTypeId : map.keySet()) {
                OcCloudInstanceType pre = CloudInstanceTypeBuilder.build(map.get(instanceTypeId));
                if (typeMap.containsKey(instanceTypeId)) {
                    // update
                    pre.setId(typeMap.get(instanceTypeId).getId());
                    ocCloudInstanceTypeService.updateOcCloudInstanceType(pre);
                    typeMap.remove(instanceTypeId);
                } else {
                    // add
                    ocCloudInstanceTypeService.addOcCloudInstanceType(pre);
                }
            }
        }
        delTypeMap(typeMap);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public List<String> queryCloudRegionList(int cloudType) {
        if (cloudType == CloudType.ALIYUN.getType())
            return aliyunCore.getRegionIds();
        return Lists.newArrayList();
    }

    @Override
    public List<Integer> queryCpuCoreList(int cloudType) {
        return ocCloudInstanceTypeService.queryCpuCoreGroup(cloudType);
    }

    private void delTypeMap(Map<String, OcCloudInstanceType> typeMap) {
        for (String instanceTypeId : typeMap.keySet())
            ocCloudInstanceTypeService.deleteOcCloudInstanceById(typeMap.get(instanceTypeId).getId());
    }

    private Map<String, OcCloudInstanceType> getInstanceTypeMap(List<OcCloudInstanceType> typeList) {
        return typeList.stream().collect(Collectors.toMap(OcCloudInstanceType::getInstanceTypeId, a -> a, (k1, k2) -> k1));
    }

}
