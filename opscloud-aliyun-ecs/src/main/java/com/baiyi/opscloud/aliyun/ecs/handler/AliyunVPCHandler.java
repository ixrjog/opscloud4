package com.baiyi.opscloud.aliyun.ecs.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.aliyun.ecs.base.BaseAliyunECS;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/18 5:43 下午
 * @Version 1.0
 */
@Component
public class AliyunVPCHandler extends BaseAliyunECS {

    /**
     * 查询所有VPC
     *
     * @param regionId
     * @return
     */
    public List<DescribeVpcsResponse.Vpc> getVPCList(String regionId) {
        List<DescribeVpcsResponse.Vpc> vpcs = Lists.newArrayList();
        try {
            DescribeVpcsRequest describe = new DescribeVpcsRequest();
            describe.setSysRegionId(regionId);
            describe.setPageSize(QUERY_PAGE_SIZE);
            int size = QUERY_PAGE_SIZE;
            int pageNumber = 1;
            while (QUERY_PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                List<DescribeVpcsResponse.Vpc> vpcList = getDescribeVpcsResponse(describe, regionId).getVpcs();
                vpcs.addAll(vpcList);
                size = vpcList.size();
                pageNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vpcs;
    }

    private DescribeVpcsResponse getDescribeVpcsResponse(DescribeVpcsRequest describe, String regionId) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            return client.getAcsResponse(describe);
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 插入vpc下的所有vSwitchs
     *
     * @param regionId
     * @param vpcId
     * @return
     */
    public List<DescribeVSwitchesResponse.VSwitch> getVSwitchList(String regionId, String vpcId) {
        List<DescribeVSwitchesResponse.VSwitch> vSwitchs = Lists.newArrayList();
        DescribeVSwitchesRequest describe = new DescribeVSwitchesRequest();
        describe.setSysRegionId(regionId);
        describe.setVpcId(vpcId);
        describe.setPageSize(QUERY_PAGE_SIZE);
        int size = QUERY_PAGE_SIZE;
        int pageNumber = 1;
        while (QUERY_PAGE_SIZE <= size) {
            describe.setPageNumber(pageNumber);
            List<DescribeVSwitchesResponse.VSwitch> vSwitchList = getDescribeVSwitchesResponse(regionId, describe).getVSwitches();
            vSwitchs.addAll(vSwitchList);
            size = vSwitchList.size();
            pageNumber++;
        }
        return vSwitchs;
    }

    /**
     * 查询阿里云VSwitches
     *
     * @param regionId
     * @param describe
     * @return
     */
    private DescribeVSwitchesResponse getDescribeVSwitchesResponse(String regionId, DescribeVSwitchesRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            DescribeVSwitchesResponse response
                    = client.getAcsResponse(describe);
            return response;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询vpc下的所有SecurityGroup
     *
     * @param regionId
     * @param vpcId
     */
    public List<DescribeSecurityGroupsResponse.SecurityGroup> getSecurityGroupList(String regionId, String vpcId) {
        List<DescribeSecurityGroupsResponse.SecurityGroup> securityGroups = Lists.newArrayList();
        DescribeSecurityGroupsRequest describe = new DescribeSecurityGroupsRequest();
        describe.setSysRegionId(regionId);
        describe.setVpcId(vpcId);

        describe.setPageSize(QUERY_PAGE_SIZE);
        int size = QUERY_PAGE_SIZE;
        int pageNumber = 1;
        while (QUERY_PAGE_SIZE <= size) {
            describe.setPageNumber(pageNumber);
            List<DescribeSecurityGroupsResponse.SecurityGroup> securityGroupList = getDescribeSecurityGroupsResponse(regionId, describe).getSecurityGroups();
            securityGroups.addAll(securityGroupList);
            size = securityGroupList.size();
            pageNumber++;
        }
        return securityGroups;
    }

    /**
     * 查询安全组
     *
     * @param regionId
     * @param describe
     * @return
     */
    private DescribeSecurityGroupsResponse getDescribeSecurityGroupsResponse(String regionId, DescribeSecurityGroupsRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            DescribeSecurityGroupsResponse response
                    = client.getAcsResponse(describe);
            return response;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }

}
