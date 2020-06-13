package com.baiyi.opscloud.cloud.vpc.impl;

import com.baiyi.opscloud.cloud.account.CloudAccount;
import com.baiyi.opscloud.cloud.vpc.ICloudVPC;
import com.baiyi.opscloud.cloud.vpc.factory.CloudVPCFactory;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpc;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcSecurityGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcVswitch;
import com.baiyi.opscloud.facade.CloudVpcBaseFacade;
import com.baiyi.opscloud.service.cloud.OcCloudVpcSecurityGroupService;
import com.baiyi.opscloud.service.cloud.OcCloudVpcService;
import com.baiyi.opscloud.service.cloud.OcCloudVpcVswitchService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/3/19 9:16 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseCloudVPC<T, VSW, SG> implements InitializingBean, ICloudVPC {

    @Resource
    private OcCloudVpcService ocCloudVpcService;

    @Resource
    private OcCloudVpcVswitchService ocCloudVpcVswitchService;

    @Resource
    private OcCloudVpcSecurityGroupService ocCloudVpcSecurityGroupService;

    @Resource
    private CloudVpcBaseFacade ocCloudVpcFacade;

    @Override
    public Boolean syncVPC() {
        CloudAccount cloudAccount = getCloudAccount();
        if (cloudAccount == null) return Boolean.FALSE;
        Map<String, OcCloudVpc> cloudVPCMap = getCloudVPCMap(Lists.newArrayList());
        List<T> cloudVPCList = getCloudVPCList();
        for (T cloudVPC : cloudVPCList) {
            saveOcCloudVpc(cloudAccount, cloudVPC, cloudVPCMap); // 录入vpc
            saveVSwitch(cloudAccount, cloudVPC);
            saveSecurityGroup(cloudAccount, cloudVPC);
        }
        // 删除旧数据
        deleteOcCloudVpcByMap(cloudVPCMap);
        return Boolean.TRUE;
    }

    // 保存VPC下的交换机信息
    private void saveVSwitch(CloudAccount account, T cloudVPC) {
        try {
            OcCloudVpc preOcCloudVpc = convertOcCloudVPC(account, cloudVPC);
            Map<String, OcCloudVpcVswitch> cloudVPCVSwitchMap = getCloudVPCVSwitchMap(preOcCloudVpc.getVpcId());
            List<VSW> vswList = getCloudVSwitchList(account, preOcCloudVpc);
            for (VSW vsw : vswList)
                saveOcCloudVpcVSwitch(preOcCloudVpc, vsw, cloudVPCVSwitchMap); // 录入vswitch
            // 删除旧数据
            deleteOcCloudVpcVSwitchByMap(cloudVPCVSwitchMap);
        } catch (Exception e) {
        }
    }

    private void saveSecurityGroup(CloudAccount account, T cloudVPC) {
        try {
            OcCloudVpc preOcCloudVpc = convertOcCloudVPC(account, cloudVPC);
            Map<String, OcCloudVpcSecurityGroup> cloudVPCSecurityGroupMap = getCloudVPCSecurityGroupMap(preOcCloudVpc.getVpcId());
            List<SG> sgList = getCloudSecurityGroupList(account, preOcCloudVpc);
            for (SG sg : sgList)
                saveOcCloudVpcSecurityGroup(preOcCloudVpc, sg, cloudVPCSecurityGroupMap); // 录入securityGroup
            // 删除旧数据
            deleteOcCloudVpcSecurityGroupByMap(cloudVPCSecurityGroupMap);
        } catch (Exception e) {
        }
    }

    private void deleteOcCloudVpcByMap(Map<String, OcCloudVpc> cloudVPCMap) {
        for (String vpcId : cloudVPCMap.keySet())
            ocCloudVpcFacade.deleteOcCloudVpc(cloudVPCMap.get(vpcId));
    }

    private void deleteOcCloudVpcVSwitchByMap(Map<String, OcCloudVpcVswitch> cloudVPCVSwitchMap) {
        for (String vswitchId : cloudVPCVSwitchMap.keySet())
            ocCloudVpcVswitchService.deleteOcCloudVpcVswitchById(cloudVPCVSwitchMap.get(vswitchId).getId());
    }

    private void deleteOcCloudVpcSecurityGroupByMap(Map<String, OcCloudVpcSecurityGroup> cloudVPCSecurityGroupMap){
        for (String securityGroupId : cloudVPCSecurityGroupMap.keySet())
            ocCloudVpcSecurityGroupService.deleteOcCloudVpcSecurityGroupById(cloudVPCSecurityGroupMap.get(securityGroupId).getId());
    }

    private void saveOcCloudVpcVSwitch(OcCloudVpc ocCloudVpc, VSW vsw, Map<String, OcCloudVpcVswitch> cloudVPCVSwitchMap) {
        try {
            String vswitchId = getVswitchId(vsw);
            if (cloudVPCVSwitchMap.containsKey(vswitchId)) {
                cloudVPCVSwitchMap.remove(vswitchId);
            } else {
                addOcCloudVpcVswitch(convertOcCloudVpcVswitch(ocCloudVpc, vsw));
            }
        } catch (Exception e) {
        }
    }

    private void saveOcCloudVpcSecurityGroup(OcCloudVpc ocCloudVpc, SG sg, Map<String, OcCloudVpcSecurityGroup> cloudVPCSecurityGroupMap) {
        try {
            String securityGroupId = getSecurityGroupId(sg);
            if (cloudVPCSecurityGroupMap.containsKey(securityGroupId)) {
                cloudVPCSecurityGroupMap.remove(securityGroupId);
            } else {
                addOcCloudVpcSecurityGroup(convertOcCloudVpcSecurityGroup(ocCloudVpc, sg));
            }
        } catch (Exception e) {
        }
    }

    private void addOcCloudVpcVswitch(OcCloudVpcVswitch ocCloudVpcVswitch) {
        ocCloudVpcVswitchService.addOcCloudVpcVswitch(ocCloudVpcVswitch);
    }

    private void addOcCloudVpcSecurityGroup(OcCloudVpcSecurityGroup ocCloudVpcSecurityGroup) {
        ocCloudVpcSecurityGroupService.addOcCloudVpcSecurityGroup(ocCloudVpcSecurityGroup);
    }

    protected void saveOcCloudVpc(CloudAccount account, T cloudVPC, Map<String, OcCloudVpc> map) {
        try {
            String vpcId = getVPCId(cloudVPC);
            if (map.containsKey(vpcId)) {
                map.remove(vpcId);
            } else {
                addOcCloudVpc(convertOcCloudVPC(account, cloudVPC));
            }
        } catch (Exception e) {
        }
    }

    private void addOcCloudVpc(OcCloudVpc ocCloudVpc) {
        ocCloudVpcService.addOcCloudVpc(ocCloudVpc);
    }

    protected Map<String, OcCloudVpc> getCloudVPCMap(List<OcCloudVpc> cloudVpcList) {
        if (CollectionUtils.isEmpty(cloudVpcList))
            cloudVpcList = ocCloudVpcService.queryOcCloudVpcByType(getCloudType());
        return cloudVpcList.stream().collect(Collectors.toMap(OcCloudVpc::getVpcId, a -> a, (k1, k2) -> k1));
    }

    private Map<String, OcCloudVpcVswitch> getCloudVPCVSwitchMap(String vpcId) {
        return ocCloudVpcVswitchService.queryOcCloudVpcVswitchByVpcId(vpcId)
                .stream().collect(Collectors.toMap(OcCloudVpcVswitch::getVswitchId, a -> a, (k1, k2) -> k1));
    }

    private Map<String, OcCloudVpcSecurityGroup> getCloudVPCSecurityGroupMap(String vpcId) {
        return ocCloudVpcSecurityGroupService.queryOcCloudVpcSecurityGroupByVpcId(vpcId)
                .stream().collect(Collectors.toMap(OcCloudVpcSecurityGroup::getSecurityGroupId, a -> a, (k1, k2) -> k1));
    }

    protected abstract List<VSW> getCloudVSwitchList(CloudAccount account, OcCloudVpc ocCloudVpc);

    protected abstract List<SG> getCloudSecurityGroupList(CloudAccount account, OcCloudVpc ocCloudVpc);

    protected abstract List<T> getCloudVPCList();

    protected abstract CloudAccount getCloudAccount();

    protected abstract int getCloudType();

    protected abstract String getVPCId(T cloudVPC) throws Exception;

    protected abstract String getVswitchId(VSW vsw) throws Exception;

    protected abstract String getSecurityGroupId(SG sg) throws Exception;

    protected abstract OcCloudVpc convertOcCloudVPC(CloudAccount account, T cloudVPC) throws Exception;

    protected abstract OcCloudVpcVswitch convertOcCloudVpcVswitch(OcCloudVpc ocCloudVpc, VSW vsw) throws Exception;

    protected abstract OcCloudVpcSecurityGroup convertOcCloudVpcSecurityGroup(OcCloudVpc ocCloudVpc, SG sg) throws Exception;

    @Override
    public String getKey() {
        return this.getClass().getSimpleName();
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        CloudVPCFactory.register(this);
    }

}
