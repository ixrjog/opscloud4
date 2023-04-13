package com.baiyi.opscloud.datasource.aliyun.redis.driver;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesRequest;
import com.aliyuncs.r_kvstore.model.v20150101.DescribeInstancesResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.datasource.aliyun.redis.entity.AliyunRedis;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.datasource.aliyun.core.SimpleAliyunClient.Query.PAGE_SIZE;

/**
 * @Author baiyi
 * @Date 2021/12/16 9:28 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunRedisInstanceDriver {

    private final AliyunClient aliyunClient;

    public static final String QUERY_ALL_INSTANCE = null;

    public List<AliyunRedis.KVStoreInstance> listInstance(String regionId, AliyunConfig.Aliyun aliyun) throws ClientException {
        return listInstance(regionId, aliyun, QUERY_ALL_INSTANCE);
    }

    public List<AliyunRedis.KVStoreInstance> listInstance(String regionId, AliyunConfig.Aliyun aliyun, String instanceIds) throws ClientException {
        List<AliyunRedis.KVStoreInstance> instances = Lists.newArrayList();
        DescribeInstancesRequest request = new  DescribeInstancesRequest();
        if (!StringUtils.isEmpty(instanceIds)) {
            request.setInstanceIds(instanceIds);
        }
        request.setPageSize(PAGE_SIZE);
        int size = PAGE_SIZE;
        int pageNumber = 1;
        while (PAGE_SIZE <= size) {
            request.setPageNumber(pageNumber);
            DescribeInstancesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            instances.addAll(BeanCopierUtil.copyListProperties(response.getInstances(),AliyunRedis.KVStoreInstance.class));
            size = response.getTotalCount();
            pageNumber++;
        }
        return instances;
    }

}