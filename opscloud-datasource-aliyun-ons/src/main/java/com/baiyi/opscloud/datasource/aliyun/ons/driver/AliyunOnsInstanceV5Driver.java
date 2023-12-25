package com.baiyi.opscloud.datasource.aliyun.ons.driver;

import com.aliyun.rocketmq20220801.Client;
import com.aliyun.rocketmq20220801.models.GetInstanceResponseBody;
import com.aliyun.rocketmq20220801.models.ListInstancesRequest;
import com.aliyun.rocketmq20220801.models.ListInstancesResponseBody;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.ons.client.AliyunOnsV5Client;
import com.baiyi.opscloud.datasource.aliyun.ons.entity.OnsInstanceV5;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.opscloud.datasource.aliyun.core.SimpleAliyunClient.Query.PAGE_SIZE;

/**
 * @Author 修远
 * @Date 2023/9/11 4:21 PM
 * @Since 1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunOnsInstanceV5Driver {

    public List<OnsInstanceV5.InstanceInfo> listInstance(String regionId, AliyunConfig.Aliyun aliyun) throws Exception {
        List<ListInstancesResponseBody.ListInstancesResponseBodyDataList> instanceList = Lists.newArrayList();
        int pageNumber = 1;
        ListInstancesRequest request = new ListInstancesRequest()
                .setPageSize(PAGE_SIZE);
        Client client = AliyunOnsV5Client.buildClient(regionId, aliyun);
        ListInstancesResponseBody.ListInstancesResponseBodyData response;
        do {
            request.setPageNumber(pageNumber);
            response = client.listInstances(request).getBody().getData();
            instanceList.addAll(response.getList());
            pageNumber++;
        } while (instanceList.size() < response.totalCount);
        return BeanCopierUtil.copyListProperties(instanceList, OnsInstanceV5.InstanceInfo.class);
    }

    public OnsInstanceV5.InstanceInfo getInstance(String regionId, AliyunConfig.Aliyun aliyun, String instanceId) throws Exception {
        Client client = AliyunOnsV5Client.buildClient(regionId, aliyun);
        GetInstanceResponseBody response = client.getInstance(instanceId).getBody();
        return BeanCopierUtil.copyProperties(response.getData(), OnsInstanceV5.InstanceInfo.class);
    }

}