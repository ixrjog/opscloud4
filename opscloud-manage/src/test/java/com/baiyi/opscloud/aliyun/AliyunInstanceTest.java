package com.baiyi.opscloud.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.ecs.model.v20140526.DescribeZonesResponse;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.ecs.AliyunInstance;
import com.baiyi.opscloud.aliyun.ecs.base.AliyunInstanceTypeVO;
import com.baiyi.opscloud.aliyun.ecs.handler.AliyunInstanceHandler;
import com.baiyi.opscloud.common.base.CloudType;
import com.baiyi.opscloud.facade.CloudInstanceFacade;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/3/19 7:03 下午
 * @Version 1.0
 */
public class AliyunInstanceTest extends BaseUnit {

    @Resource
    private AliyunInstanceHandler aliyunInstanceHandler;

    @Resource
    private AliyunInstance aliyunInstance;

    @Resource
    private  CloudInstanceFacade cloudInstanceFacade;

    @Resource
    private AliyunCore aliyunCore;

    @Test
    void testGetType() {
//        List<DescribeInstanceTypesResponse.InstanceType> list
//                = aliyunInstanceHandler.getInstanceTypeList("cn-hangzhou");
//        System.err.println(JSON.toJSONString(list));
        Map<String, AliyunInstanceTypeVO.InstanceType> map = aliyunInstance.getInstanceTypeMap();
        System.err.println(JSON.toJSONString(map));
    }


    @Test
    void testGetZone() {
        List<DescribeZonesResponse.Zone> list
                = aliyunInstanceHandler.getZoneList("cn-hangzhou");
        Map<String, Set<String>> map = Maps.newHashMap();
        for (DescribeZonesResponse.Zone zone : list) {
            for (String type : zone.getAvailableInstanceTypes()) {
                if (map.containsKey(type)) {
                    map.get(type).add(zone.getZoneId());
                } else {
                    Set<String> set = Sets.newSet();
                    set.add(zone.getZoneId());
                    map.put(type, set);
                }
            }
        }
        System.err.println(JSON.toJSONString(map));
    }

    @Test
    void testSyncType(){
        cloudInstanceFacade.syncInstanceType(CloudType.ALIYUN.getType());
    }


}
