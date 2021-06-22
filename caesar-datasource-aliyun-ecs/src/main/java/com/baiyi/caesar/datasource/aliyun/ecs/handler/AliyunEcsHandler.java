package com.baiyi.caesar.datasource.aliyun.ecs.handler;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.caesar.common.datasource.config.DsAliyunConfig;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/18 9:46 上午
 * @Version 1.0
 */
@Component
public class AliyunEcsHandler {

    @Resource
    private AliyunInstanceHandler aliyunInstanceHandler;

    public List<DescribeInstancesResponse.Instance> listInstance(String regionId, DsAliyunConfig.Aliyun aliyun) {
        List<DescribeInstancesResponse.Instance> instanceList = Lists.newArrayList();
        String nextToken;
        try {
            DescribeInstancesRequest describe = new DescribeInstancesRequest();
            do {
                DescribeInstancesResponse response = aliyunInstanceHandler.getAcsResponse(regionId, aliyun, describe);
                instanceList.addAll(response.getInstances());
                nextToken = response.getNextToken();
                describe.setNextToken(nextToken);
            } while (Strings.isNotBlank(nextToken));
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return instanceList;
    }
}
