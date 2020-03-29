package com.baiyi.opscloud.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupsResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.ecs.handler.AliyunVPCHandler;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/18 6:11 下午
 * @Version 1.0
 */
public class AliyunVPCTest extends BaseUnit {

    @Resource
    private AliyunVPCHandler aliyunVPCHandler;

    @Resource
    private AliyunCore aliyunCore;

    @Test
    void testGetVPC() {
        for (String regionId : aliyunCore.getRegionIds()) {
            List<DescribeVpcsResponse.Vpc> list = aliyunVPCHandler.getVPCList(regionId);
            System.err.println(JSON.toJSONString(list));
        }
    }

    @Test
    void testGetVSwitch() {
        List<DescribeVSwitchesResponse.VSwitch> list = aliyunVPCHandler.getVSwitchList("cn-hangzhou", "vpc-bp1hw706uv9bcan1h2emq");
        System.err.println(JSON.toJSONString(list));
    }

    @Test
    void testGetSecurityGroup() {
        List<DescribeSecurityGroupsResponse.SecurityGroup> list = aliyunVPCHandler.getSecurityGroupList("cn-hangzhou", "vpc-bp1hw706uv9bcan1h2emq");
        System.err.println(JSON.toJSONString(list));
    }
}
