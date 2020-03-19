package com.baiyi.opscloud.aliyun.ecs.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstanceTypesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeZonesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeZonesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/19 6:08 下午
 * @Version 1.0
 */
@Component
public class AliyunInstanceHandler {

    @Resource
    private AliyunCore aliyunCore;

    public List<DescribeZonesResponse.Zone> getZoneList(String regionId) {
        try {
            DescribeZonesRequest describe = new DescribeZonesRequest();
            describe.setSysRegionId(regionId);
            DescribeZonesResponse response = getDescribeZonesResponse(regionId,describe);
            return response.getZones();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    private DescribeZonesResponse getDescribeZonesResponse(String regionId,DescribeZonesRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            DescribeZonesResponse response
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
     * 查询可用取实例类型
     * @param regionId
     * @return
     */
    public List<DescribeInstanceTypesResponse.InstanceType> getInstanceTypeList(String regionId) {
        try {
            DescribeInstanceTypesRequest describe = new DescribeInstanceTypesRequest();
            describe.setSysRegionId(regionId);
            DescribeInstanceTypesResponse response = getDescribeInstanceTypesResponse(regionId, describe);
            return response.getInstanceTypes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private DescribeInstanceTypesResponse getDescribeInstanceTypesResponse(String regionId, DescribeInstanceTypesRequest describe) {
        IAcsClient client = acqAcsClient(regionId);
        try {
            DescribeInstanceTypesResponse response
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
