package com.baiyi.opscloud.aliyun.ecs.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/18 5:43 下午
 * @Version 1.0
 */
@Component
public class AliyunVPCHandler {

    @Resource
    private AliyunCore aliyunCore;

    public static final int QUERY_PAGE_SIZE = 50;

    /**
     * 查询所有VPC
     *
     * @param regionId
     * @return
     */
    public List<DescribeVpcsResponse.Vpc> getVPCList(String regionId) {
        List<DescribeVpcsResponse.Vpc> vpcList = Lists.newArrayList();
        try {
            DescribeVpcsRequest describe = new DescribeVpcsRequest();
            describe.setRegionId(regionId);
            describe.setPageSize(QUERY_PAGE_SIZE);
            int size = QUERY_PAGE_SIZE;
            int pageNumber = 1;
            while (QUERY_PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeVpcsResponse response = getDescribeVpcsResponse(describe, regionId);
                vpcList.addAll(response.getVpcs());
                size = response.getVpcs().size();
                pageNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vpcList;
    }

    private DescribeVpcsResponse getDescribeVpcsResponse(DescribeVpcsRequest describe, String regionId) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            DescribeVpcsResponse response
                    = client.getAcsResponse(describe);
            return response;
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
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
        List<DescribeVSwitchesResponse.VSwitch> vSwitchList = Lists.newArrayList();
        DescribeVSwitchesRequest describe = new DescribeVSwitchesRequest();
        describe.setSysRegionId(regionId);
        describe.setVpcId(vpcId);
        describe.setPageSize(QUERY_PAGE_SIZE);
        int size = QUERY_PAGE_SIZE;
        int pageNumber = 1;
        while (QUERY_PAGE_SIZE <= size) {
            describe.setPageNumber(pageNumber);
            DescribeVSwitchesResponse response = getDescribeVSwitchesResponse(regionId, describe);
            vSwitchList.addAll(response.getVSwitches());
            size = response.getVSwitches().size();
            pageNumber++;
        }
        return vSwitchList;
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
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
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
        List<DescribeSecurityGroupsResponse.SecurityGroup> securityGroupList = Lists.newArrayList();
        DescribeSecurityGroupsRequest describe = new DescribeSecurityGroupsRequest();
        describe.setSysRegionId(regionId);
        describe.setVpcId(vpcId);

        describe.setPageSize(QUERY_PAGE_SIZE);
        int size = QUERY_PAGE_SIZE;
        int pageNumber = 1;
        while (QUERY_PAGE_SIZE <= size) {
            describe.setPageNumber(pageNumber);
            DescribeSecurityGroupsResponse response = getDescribeSecurityGroupsResponse(regionId, describe);
            securityGroupList.addAll(response.getSecurityGroups());
            size = response.getSecurityGroups().size();
            pageNumber++;
        }
        return securityGroupList;
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
        } catch (ServerException e) {
            e.printStackTrace();
            return null;
        } catch (ClientException e) {
            e.printStackTrace();
            return null;
        }
    }


    private IAcsClient acqAcsClient(String regionId) {
        return aliyunCore.getAcsClient(regionId);
    }
}
