package com.baiyi.opscloud.cloud.vpc.impl;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.aliyun.ecs.handler.AliyunVPCHandler;
import com.baiyi.opscloud.cloud.account.CloudAccount;
import com.baiyi.opscloud.cloud.vpc.ICloudVPC;
import com.baiyi.opscloud.cloud.vpc.builder.OcCloudVpcBuilder;
import com.baiyi.opscloud.cloud.vpc.builder.OcCloudVpcSecurityGroupBuilder;
import com.baiyi.opscloud.cloud.vpc.builder.OcCloudVpcVswitchBuilder;
import com.baiyi.opscloud.common.base.CloudType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpc;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcSecurityGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudVpcVswitch;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/19 9:20 上午
 * @Version 1.0
 */
@Slf4j
@Component("AliyunCloudVPC")
public class AliyunCloudVPC<T, VSW, SG> extends BaseCloudVPC<T, VSW, SG> implements ICloudVPC {

    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private AliyunVPCHandler aliyunVPCHandler;

    @Override
    protected int getCloudType() {
        return CloudType.ALIYUN.getType();
    }

    @Override
    protected List<T> getCloudVPCList() {
        List<DescribeVpcsResponse.Vpc> cloudVPCList = Lists.newArrayList();
        for (String regionId : aliyunCore.getRegionIds())
            cloudVPCList.addAll(aliyunVPCHandler.getVPCList(regionId));
        return (List<T>) cloudVPCList;
    }

    @Override
    protected List<VSW> getCloudVSwitchList(CloudAccount account, OcCloudVpc ocCloudVpc) {
        return (List<VSW>) aliyunVPCHandler.getVSwitchList(ocCloudVpc.getRegionId(), ocCloudVpc.getVpcId());
    }

    @Override
    protected List<SG> getCloudSecurityGroupList(CloudAccount account, OcCloudVpc ocCloudVpc) {
        return (List<SG>) aliyunVPCHandler.getSecurityGroupList(ocCloudVpc.getRegionId(), ocCloudVpc.getVpcId());
    }

    @Override
    protected String getVPCId(T cloudVPC) throws Exception {
        if (!(cloudVPC instanceof DescribeVpcsResponse.Vpc)) throw new Exception();
        return ((DescribeVpcsResponse.Vpc) cloudVPC).getVpcId();

    }

    @Override
    protected String getVswitchId(VSW vsw) throws Exception {
        if (!(vsw instanceof DescribeVSwitchesResponse.VSwitch)) throw new Exception();
        return ((DescribeVSwitchesResponse.VSwitch) vsw).getVSwitchId();
    }

    @Override
    protected String getSecurityGroupId(SG sg) throws Exception{
        if (!(sg instanceof DescribeSecurityGroupsResponse.SecurityGroup)) throw new Exception();
        return ((DescribeSecurityGroupsResponse.SecurityGroup) sg).getSecurityGroupId();
    }

    @Override
    protected OcCloudVpc convertOcCloudVPC(CloudAccount account, T cloudVPC) throws Exception {
        if (!(cloudVPC instanceof DescribeVpcsResponse.Vpc)) return null;
        return OcCloudVpcBuilder.build(account, (DescribeVpcsResponse.Vpc) cloudVPC);
    }

    @Override
    protected OcCloudVpcVswitch convertOcCloudVpcVswitch(OcCloudVpc ocCloudVpc, VSW vsw) throws Exception {
        if (!(vsw instanceof DescribeVSwitchesResponse.VSwitch)) return null;
        return OcCloudVpcVswitchBuilder.build(ocCloudVpc, (DescribeVSwitchesResponse.VSwitch) vsw);
    }

    @Override
    protected OcCloudVpcSecurityGroup convertOcCloudVpcSecurityGroup(OcCloudVpc ocCloudVpc, SG sg) throws Exception{
        if (!(sg instanceof DescribeSecurityGroupsResponse.SecurityGroup)) return null;
        return OcCloudVpcSecurityGroupBuilder.build(ocCloudVpc, (DescribeSecurityGroupsResponse.SecurityGroup) sg);
    }

    @Override
    protected CloudAccount getCloudAccount() {
        AliyunCoreConfig.AliyunAccount account = aliyunCore.getAccount();
        if (account == null) return null;
        return BeanCopierUtils.copyProperties(account, CloudAccount.class);
    }

}
