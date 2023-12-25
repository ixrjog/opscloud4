package com.baiyi.opscloud.datasource.aliyun.ecs.driver;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.datasource.aliyun.core.SimpleAliyunClient.Query.PAGE_SIZE;

/**
 * @Author 修远
 * @Date 2021/6/23 1:22 下午
 * @Since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunVpcDriver {

    private final AliyunClient aliyunClient;

    public List<DescribeVpcsResponse.Vpc> listVpcs(String regionId, AliyunConfig.Aliyun aliyun) {
        List<DescribeVpcsResponse.Vpc> vpcs = Lists.newArrayList();
        try {
            DescribeVpcsRequest describe = new DescribeVpcsRequest();
            describe.setSysRegionId(regionId);
            describe.setPageSize(PAGE_SIZE);
            int size = PAGE_SIZE;
            int pageNumber = 1;
            while (PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeVpcsResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
                vpcs.addAll(response.getVpcs());
                size = response.getVpcs().size();
                pageNumber++;
            }
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return vpcs;
    }

    public List<DescribeVSwitchesResponse.VSwitch> listVSwitches(String regionId, AliyunConfig.Aliyun aliyun, String vpcId) {
        List<DescribeVSwitchesResponse.VSwitch> vSwitches = Lists.newArrayList();
        try {
            DescribeVSwitchesRequest describe = new DescribeVSwitchesRequest();
            describe.setSysRegionId(regionId);
            describe.setPageSize(PAGE_SIZE);
            describe.setVpcId(vpcId);
            int size = PAGE_SIZE;
            int pageNumber = 1;
            while (PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeVSwitchesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
                vSwitches.addAll(response.getVSwitches());
                size = response.getVSwitches().size();
                pageNumber++;
            }
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return vSwitches;
    }

}