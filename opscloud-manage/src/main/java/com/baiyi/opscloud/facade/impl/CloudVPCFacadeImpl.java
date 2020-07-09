package com.baiyi.opscloud.facade.impl;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.baiyi.opscloud.aliyun.ecs.AliyunInstance;
import com.baiyi.opscloud.cloud.vpc.ICloudVPC;
import com.baiyi.opscloud.cloud.vpc.factory.CloudVPCFactory;
import com.baiyi.opscloud.common.base.CloudType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.cloud.CloudVPCDecorator;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpc;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcSecurityGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcVswitch;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCParam;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCSecurityGroupParam;
import com.baiyi.opscloud.domain.param.cloud.CloudVPCVSwitchParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudInstanceTemplateVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudVPCSecurityGroupVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudVPCVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudVSwitchVO;
import com.baiyi.opscloud.facade.CloudVPCFacade;
import com.baiyi.opscloud.service.cloud.OcCloudVpcSecurityGroupService;
import com.baiyi.opscloud.service.cloud.OcCloudVpcService;
import com.baiyi.opscloud.service.cloud.OcCloudVpcVswitchService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/18 7:02 下午
 * @Version 1.0
 */
@Service
public class CloudVPCFacadeImpl implements CloudVPCFacade {

    @Resource
    private OcCloudVpcService ocCloudVpcService;

    @Resource
    private OcCloudVpcSecurityGroupService ocCloudVpcSecurityGroupService;

    @Resource
    private CloudVPCDecorator cloudVPCDecorator;

    @Resource
    private OcCloudVpcVswitchService ocCloudVpcVswitchService;

    @Resource
    private AliyunInstance aliyunInstance;


    @Override
    public DataTable<CloudVPCVO.CloudVpc> fuzzyQueryCloudVPCPage(CloudVPCParam.PageQuery pageQuery) {
        DataTable<OcCloudVpc> table = ocCloudVpcService.fuzzyQueryOcCloudVpcByParam(pageQuery);
        List<CloudVPCVO.CloudVpc> page = BeanCopierUtils.copyListProperties(table.getData(), CloudVPCVO.CloudVpc.class);
        return new DataTable<>(page.stream().map(e -> cloudVPCDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());

    }

    @Override
    public DataTable<CloudVPCVO.CloudVpc> queryCloudVPCPage(CloudVPCParam.PageQuery pageQuery) {
        DataTable<OcCloudVpc> table = ocCloudVpcService.fuzzyQueryOcCloudVpcByParam(pageQuery);
        List<CloudVPCVO.CloudVpc> page = BeanCopierUtils.copyListProperties(table.getData(), CloudVPCVO.CloudVpc.class);
        return new DataTable<>(page.stream().map(e -> cloudVPCDecorator.decorator(e, pageQuery.getZoneIds())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public DataTable<CloudVPCSecurityGroupVO.SecurityGroup> queryCloudVPCSecurityGroupPage(CloudVPCSecurityGroupParam.PageQuery pageQuery) {
        DataTable<OcCloudVpcSecurityGroup> table = ocCloudVpcSecurityGroupService.queryOcCloudVPCSecurityGroupByParam(pageQuery);
        List<CloudVPCSecurityGroupVO.SecurityGroup> page = BeanCopierUtils.copyListProperties(table.getData(), CloudVPCSecurityGroupVO.SecurityGroup.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public DataTable<CloudVSwitchVO.VSwitch> queryCloudVPCVSwitchPage(CloudVPCVSwitchParam.PageQuery pageQuery) {
        DataTable<OcCloudVpcVswitch> table = ocCloudVpcVswitchService.queryOcCloudVPCVswitchByParam(pageQuery);
        List<CloudVSwitchVO.VSwitch> page = BeanCopierUtils.copyListProperties(table.getData(), CloudVSwitchVO.VSwitch.class);
        return new DataTable<>(page, table.getTotalNum());
    }

    @Override
    public BusinessWrapper<Boolean> syncCloudVPCByKey(String key) {
        ICloudVPC cloudVPC = CloudVPCFactory.getCloudVPCByKey(key);
        return new BusinessWrapper<>(cloudVPC.syncVPC());
    }

    @Override
    public BusinessWrapper<Boolean> deleteCloudVPCById(int id) {
        OcCloudVpc ocCloudVpc = ocCloudVpcService.queryOcCloudVpcById(id);
        if (ocCloudVpc == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_VPC_NOT_EXIST);
        ocCloudVpcService.deleteOcCloudVpcById(id);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> setCloudVPCActive(int id) {
        OcCloudVpc ocCloudVpc = ocCloudVpcService.queryOcCloudVpcById(id);
        if (ocCloudVpc == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_VPC_NOT_EXIST);
        ocCloudVpc.setIsActive(ocCloudVpc.getIsActive() == 0 ? 1 : 0);
        ocCloudVpcService.updateOcCloudVpc(ocCloudVpc);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> setCloudVPCSecurityGroupActive(int id) {
        OcCloudVpcSecurityGroup ocCloudVpcSecurityGroup = ocCloudVpcSecurityGroupService.queryOcCloudVpcSecurityGroupById(id);
        if (ocCloudVpcSecurityGroup == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_VPC_SECURITY_GROUP_NOT_EXIST);
        ocCloudVpcSecurityGroup.setIsActive(ocCloudVpcSecurityGroup.getIsActive() == 0 ? 1 : 0);
        ocCloudVpcSecurityGroupService.updateOcCloudVpcSecurityGroup(ocCloudVpcSecurityGroup);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> setCloudVPCVSwitchActive(int id) {
        OcCloudVpcVswitch ocCloudVpcVswitch = ocCloudVpcVswitchService.queryOcCloudVpcVswitchById(id);
        if (ocCloudVpcVswitch == null)
            return new BusinessWrapper<>(ErrorEnum.CLOUD_VPC_VSWITCH_NOT_EXIST);
        ocCloudVpcVswitch.setIsActive(ocCloudVpcVswitch.getIsActive() == 0 ? 1 : 0);
        ocCloudVpcVswitchService.updateOcCloudVpcVswitch(ocCloudVpcVswitch);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public List<CloudVSwitchVO.VSwitch> updateOcCloudVpcVSwitch(CloudInstanceTemplateVO.InstanceTemplate instanceTemplate, List<CloudInstanceTemplateVO.VSwitch> vswitchList) {
        List<CloudVSwitchVO.VSwitch> result = Lists.newArrayList();
        Set<String> vswitchIdSet = vswitchList.stream().map(CloudInstanceTemplateVO.VSwitch::getVswitchId).collect(Collectors.toSet());
        if (instanceTemplate.getCloudType() == CloudType.ALIYUN.getType()) {
            Map<String, DescribeVSwitchesResponse.VSwitch> AliyunVPCVSwitchMap = getAliyunVPCVSwitchMap(instanceTemplate.getRegionId(), instanceTemplate.getVpcId());
            ocCloudVpcVswitchService.queryOcCloudVpcVswitchByVpcId(instanceTemplate.getVpcId()).forEach(e -> {
                if (AliyunVPCVSwitchMap.containsKey(e.getVswitchId())) {
                    DescribeVSwitchesResponse.VSwitch vSwitch = AliyunVPCVSwitchMap.get(e.getVswitchId());
                    e.setVswitchName(vSwitch.getVSwitchName());
                    e.setAvailableIpAddressCount(vSwitch.getAvailableIpAddressCount().intValue());
                    ocCloudVpcVswitchService.updateOcCloudVpcVswitch(e);
                    if (vswitchIdSet.contains(e.getVswitchId()))
                        result.add(BeanCopierUtils.copyProperties(e, CloudVSwitchVO.VSwitch.class));
                }

            });
        }
        return result;
    }

    private Map<String, DescribeVSwitchesResponse.VSwitch> getAliyunVPCVSwitchMap(String regionId, String vpcId) {
        List<DescribeVSwitchesResponse.VSwitch> list = aliyunInstance.getVSwitchList(regionId, vpcId);
        return list.stream().collect(Collectors.toMap(DescribeVSwitchesResponse.VSwitch::getVSwitchId, e -> e, (k1, k2) -> k1));
    }


}
