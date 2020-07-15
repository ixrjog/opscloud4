package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.ecs.AliyunInstance;
import com.baiyi.opscloud.aliyun.ecs.base.AliyunInstanceTypeVO;
import com.baiyi.opscloud.bo.CreateCloudInstanceBO;
import com.baiyi.opscloud.builder.CloudInstanceTaskBuilder;
import com.baiyi.opscloud.builder.CloudInstanceTemplateBuilder;
import com.baiyi.opscloud.builder.CloudInstanceTypeBuilder;
import com.baiyi.opscloud.common.base.CloudType;
import com.baiyi.opscloud.common.base.LoginType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.CloudInstanceTemplateUtils;
import com.baiyi.opscloud.common.util.IDUtils;
import com.baiyi.opscloud.common.util.SessionUtils;
import com.baiyi.opscloud.decorator.cloud.CloudInstanceTemplateDecorator;
import com.baiyi.opscloud.decorator.cloud.CloudInstanceTypeDecorator;
import com.baiyi.opscloud.decorator.cloud.InstanceTemplateDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTemplateParam;
import com.baiyi.opscloud.domain.param.cloud.CloudInstanceTypeParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTemplateVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTypeVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudVSwitchVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.CloudInstanceFacade;
import com.baiyi.opscloud.facade.CloudInstanceTaskFacade;
import com.baiyi.opscloud.facade.CloudVPCFacade;
import com.baiyi.opscloud.service.cloud.*;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.user.OcUserService;
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
    private InstanceTemplateDecorator instanceTemplateDecorator;

    @Resource
    private AliyunInstance aliyunInstance;

    @Resource
    private CloudVPCFacade cloudVPCFacade;

    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcCloudImageService ocCloudImageService;

    @Resource
    private OcCloudVpcSecurityGroupService ocCloudVpcSecurityGroupService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcCloudInstanceTaskService ocCloudInstanceTaskService;

    @Resource
    private CloudInstanceTaskFacade cloudInstanceTaskFacade;

    @Override
    public DataTable<CloudInstanceTemplateVO.CloudInstanceTemplate> fuzzyQueryCloudInstanceTemplatePage(CloudInstanceTemplateParam.PageQuery pageQuery) {
        DataTable<OcCloudInstanceTemplate> table = ocCloudInstanceTemplateService.fuzzyQueryOcCloudInstanceTemplateByParam(pageQuery);
        DataTable<CloudInstanceTemplateVO.CloudInstanceTemplate> dataTable
                = new DataTable<>(table.getData().stream().map(e -> cloudInstanceTemplateDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public BusinessWrapper<CloudInstanceTemplateVO.CloudInstanceTemplate> saveCloudInstanceTemplate(CloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {
        OcCloudInstanceTemplate pre = BeanCopierUtils.copyProperties(cloudInstanceTemplate, OcCloudInstanceTemplate.class);
        if (pre.getCloudType() == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_TYPE_IS_NULL);
        if (StringUtils.isEmpty(cloudInstanceTemplate.getTemplateName()))
            return new BusinessWrapper<>(ErrorEnum.CLOUD_INSTANCE_TEMPLATE_NAME_NON_COMPLIANCE_WITH_RULES);
        // 重新组装
        CloudInstanceTemplateVO.InstanceTemplate instanceTemplate = instanceTemplateDecorator.decorator(cloudInstanceTemplate);
        Yaml yaml = new Yaml();
        pre.setTemplateYaml(yaml.dumpAs(instanceTemplate, Tag.MAP, DumperOptions.FlowStyle.BLOCK));// 序列化对象为YAML-BLOCK格式

        if (IDUtils.isEmpty(pre.getId())) {
            ocCloudInstanceTemplateService.addOcCloudInstanceTemplate(pre);
        } else {
            ocCloudInstanceTemplateService.updateOcCloudInstanceTemplate(pre);
        }
        return new BusinessWrapper(cloudInstanceTemplateDecorator.decorator(pre));
    }

    @Override
    public BusinessWrapper<Integer> createCloudInstance(CloudInstanceTemplateParam.CreateCloudInstance createCloudInstance) {
        // 校验模版
        OcCloudInstanceTemplate ocCloudInstanceTemplate = ocCloudInstanceTemplateService.queryOcCloudInstanceTemplateById(createCloudInstance.getTemplateId());
        if (ocCloudInstanceTemplate == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_INSTANCE_TEMPLATE_NOT_EXIST);
        // 校验云镜像
        OcCloudImage ocCloudImage = ocCloudImageService.queryOcCloudImageByImageId(createCloudInstance.getImageId());
        if (ocCloudImage == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_IMAGE_NOT_EXIST);
        // 校验安全组
        OcCloudVpcSecurityGroup ocCloudVpcSecurityGroup = ocCloudVpcSecurityGroupService.queryOcCloudVpcSecurityGroupBySecurityGroupId(createCloudInstance.getSecurityGroupId());
        if (ocCloudVpcSecurityGroup == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_VPC_SECURITY_GROUP_NOT_EXIST);
        if (createCloudInstance.getZonePattern().equals("single")) {
            // 校验可用区
            if (StringUtils.isEmpty(createCloudInstance.getZoneId()))
                return new BusinessWrapper<>(ErrorEnum.CREATE_CLOUD_INSTANCE_ZONEID_MUST_BE_SELECTED);
            // 校验虚拟交换机
            if (createCloudInstance.getVswitchIds().isEmpty())
                return new BusinessWrapper<>(ErrorEnum.CREATE_CLOUD_INSTANCE_VSWITCHIDS_MUST_BE_SELECTED);
        }
        // 校验服务器组
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(createCloudInstance.getServerGroupId());
        if (ocServerGroup == null)
            return new BusinessWrapper<>(ErrorEnum.SERVER_GROUP_NOT_SELECTED);
        if (createCloudInstance.getLoginType() == null)
            createCloudInstance.setLoginType(LoginType.KEY.getType());
        if (createCloudInstance.getCreateSize() == null)
            createCloudInstance.setCreateSize(1);

        CreateCloudInstanceBO createCloudInstanceBO = CreateCloudInstanceBO.builder()
                .createCloudInstance(createCloudInstance)
                .cloudInstanceTemplate(cloudInstanceTemplateDecorator.decorator(ocCloudInstanceTemplate))
                .ocCloudImage(ocCloudImage)
                .ocCloudVpcSecurityGroup(ocCloudVpcSecurityGroup)
                .ocServerGroup(ocServerGroup)
                .build();

        OcUser ocUser = ocUserService.queryOcUserByUsername(SessionUtils.getUsername());
        OcCloudInstanceTask ocCloudInstanceTask = ocUser == null ? CloudInstanceTaskBuilder.build(createCloudInstanceBO)
                : CloudInstanceTaskBuilder.build(createCloudInstanceBO, BeanCopierUtils.copyProperties(ocUser, UserVO.User.class));
        ocCloudInstanceTaskService.addOcCloudInstanceTask(ocCloudInstanceTask);
        // 执行任务
        cloudInstanceTaskFacade.doCreateInstanceTask(ocCloudInstanceTask, createCloudInstanceBO);
        return new BusinessWrapper(ocCloudInstanceTask.getId());
    }


    @Override
    public BusinessWrapper<CloudInstanceTemplateVO.CloudInstanceTemplate> saveCloudInstanceTemplateYAML(CloudInstanceTemplateVO.CloudInstanceTemplate cloudInstanceTemplate) {
        // YAML对象
        CloudInstanceTemplateVO.InstanceTemplate instanceTemplate = CloudInstanceTemplateUtils.convert(cloudInstanceTemplate.getTemplateYAML());
        // 通过YAML对象组装模版
        OcCloudInstanceTemplate pre = CloudInstanceTemplateBuilder.build(instanceTemplate, cloudInstanceTemplate.getId());
        if (pre.getCloudType() == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_TYPE_IS_NULL);
        if (StringUtils.isEmpty(cloudInstanceTemplate.getTemplateName()))
            return new BusinessWrapper<>(ErrorEnum.CLOUD_INSTANCE_TEMPLATE_NAME_NON_COMPLIANCE_WITH_RULES);
        if (IDUtils.isEmpty(pre.getId())) {
            ocCloudInstanceTemplateService.addOcCloudInstanceTemplate(pre);
        } else {
            ocCloudInstanceTemplateService.updateOcCloudInstanceTemplate(pre);
        }
        return new BusinessWrapper(cloudInstanceTemplateDecorator.decorator(pre));
    }

    @Override
    public DataTable<CloudInstanceTypeVO.CloudInstanceType> fuzzyQueryCloudInstanceTypePage(CloudInstanceTypeParam.PageQuery pageQuery) {
        DataTable<OcCloudInstanceType> table = ocCloudInstanceTypeService.fuzzyQueryOcCloudInstanceTypeByParam(pageQuery);
        List<CloudInstanceTypeVO.CloudInstanceType> page = BeanCopierUtils.copyListProperties(table.getData(), CloudInstanceTypeVO.CloudInstanceType.class);
        DataTable<CloudInstanceTypeVO.CloudInstanceType> dataTable
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

    @Override
    public List<CloudVSwitchVO.VSwitch> queryCloudInstanceTemplateVSwitch(int templateId, String zoneId) {
        OcCloudInstanceTemplate ocCloudInstanceTemplate = ocCloudInstanceTemplateService.queryOcCloudInstanceTemplateById(templateId);
        CloudInstanceTemplateVO.InstanceTemplate instanceTemplate = CloudInstanceTemplateUtils.convert(ocCloudInstanceTemplate.getTemplateYaml());
        List<CloudInstanceTemplateVO.VSwitch> vswitchList = instanceTemplate.getVswitchs();
        filterVSwitchListByZoneId(zoneId, vswitchList);
        return cloudVPCFacade.updateOcCloudVpcVSwitch(instanceTemplate, vswitchList);
    }

    /**
     * 过滤非此可用区的虚拟交换机
     *
     * @param zoneId
     * @param vswitchList
     */
    private void filterVSwitchListByZoneId(String zoneId, List<CloudInstanceTemplateVO.VSwitch> vswitchList) {
        vswitchList.removeIf(vSwitch -> !zoneId.equals(vSwitch.getZoneId()));
    }

    private void delTypeMap(Map<String, OcCloudInstanceType> typeMap) {
        typeMap.keySet().forEach(k -> ocCloudInstanceTypeService.deleteOcCloudInstanceById(typeMap.get(k).getId()));
    }

    private Map<String, OcCloudInstanceType> getInstanceTypeMap(List<OcCloudInstanceType> typeList) {
        return typeList.stream().collect(Collectors.toMap(OcCloudInstanceType::getInstanceTypeId, a -> a, (k1, k2) -> k1));
    }


}
