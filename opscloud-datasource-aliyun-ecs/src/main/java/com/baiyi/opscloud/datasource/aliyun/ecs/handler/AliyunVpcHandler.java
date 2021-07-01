package com.baiyi.opscloud.datasource.aliyun.ecs.handler;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.config.DsAliyunConfig;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.baiyi.opscloud.datasource.aliyun.ecs.common.BaseAliyunHandler.Query.PAGE_SIZE;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/23 1:22 下午
 * @Since 1.0
 */
@Component
public class AliyunVpcHandler {

    @Resource
    private AliyunHandler aliyunHandler;

    public List<DescribeVpcsResponse.Vpc> listVpcs(String regionId, DsAliyunConfig.Aliyun aliyun) {
        List<DescribeVpcsResponse.Vpc> vpcs = Lists.newArrayList();
        try {
            DescribeVpcsRequest describe = new DescribeVpcsRequest();
            describe.setSysRegionId(regionId);
            describe.setPageSize(PAGE_SIZE);
            int size = PAGE_SIZE;
            int pageNumber = 1;
            while (PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeVpcsResponse response = aliyunHandler.getAcsResponse(regionId, aliyun, describe);
                vpcs.addAll(response.getVpcs());
                size = response.getVpcs().size();
                pageNumber++;
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return vpcs;
    }

    public List<DescribeVSwitchesResponse.VSwitch> listVSwitches(String regionId, DsAliyunConfig.Aliyun aliyun) {
        List<DescribeVSwitchesResponse.VSwitch> vSwitches = Lists.newArrayList();
        try {
            DescribeVSwitchesRequest describe = new DescribeVSwitchesRequest();
            describe.setSysRegionId(regionId);
            describe.setPageSize(PAGE_SIZE);
            int size = PAGE_SIZE;
            int pageNumber = 1;
            while (PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeVSwitchesResponse response = aliyunHandler.getAcsResponse(regionId, aliyun, describe);
                vSwitches.addAll(response.getVSwitches());
                size = response.getVSwitches().size();
                pageNumber++;
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return vSwitches;
    }

}
