package com.baiyi.opscloud.aliyun.slb.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.slb.model.v20140515.*;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 11:16 上午
 * @Since 1.0
 */
@Slf4j
@Component("AliyunSLBHandler")
public class AliyunSLBHandler {

    @Resource
    private AliyunCore aliyunCore;

    public List<DescribeLoadBalancersResponse.LoadBalancer> queryLoadBalancerList() {
        int pageSize = 50;
        DescribeLoadBalancersRequest describe = new DescribeLoadBalancersRequest();
        describe.setPageSize(pageSize);
        DescribeLoadBalancersResponse response = getDescribeLoadBalancerResponse(describe);
        assert response != null;
        List<DescribeLoadBalancersResponse.LoadBalancer> loadBalancerList = Lists.newArrayList(response.getLoadBalancers());
        // 获取总数
        int totalCount = response.getTotalCount();
        // 循环次数
        int cnt = (totalCount + pageSize - 1) / pageSize;
        for (int i = 1; i < cnt; i++) {
            describe.setPageNumber(i + 1);
            response = getDescribeLoadBalancerResponse(describe);
            loadBalancerList.addAll(response.getLoadBalancers());
        }
        return loadBalancerList;
    }

    private DescribeLoadBalancersResponse getDescribeLoadBalancerResponse(DescribeLoadBalancersRequest request) {
        IAcsClient client = aliyunCore.getMasterClient();
        try {
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("AliyunSLBHandler.getDescribeLoadBalancerResponse() 失败", e);
        }
        return null;
    }

    public List<DescribeLoadBalancerAttributeResponse.ListenerPortAndProtocol> queryLoadBalancerListener(String loadBalancerId) {
        IAcsClient client = aliyunCore.getMasterClient();
        DescribeLoadBalancerAttributeRequest request = new DescribeLoadBalancerAttributeRequest();
        request.setLoadBalancerId(loadBalancerId);
        try {
            DescribeLoadBalancerAttributeResponse response = client.getAcsResponse(request);
            return response.getListenerPortsAndProtocol();
        } catch (ClientException e) {
            log.error("AliyunSLBHandler.queryLoadBalancerListener() 失败", e);
        }
        return Collections.emptyList();
    }

    public DescribeLoadBalancerHTTPSListenerAttributeResponse queryHttpsListenerDetail(AliyunSLBParam.HttpsListenerQuery param) {
        IAcsClient client = aliyunCore.getMasterClient();
        DescribeLoadBalancerHTTPSListenerAttributeRequest request = new DescribeLoadBalancerHTTPSListenerAttributeRequest();
        request.setListenerPort(param.getHttpsListenerPort());
        request.setLoadBalancerId(param.getLoadBalancerId());
        try {
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("AliyunSLBHandler.queryHttpsListenerDetail() 失败", e);
        }
        return null;
    }

    public List<DescribeHealthStatusResponse.BackendServer> queryLoadBalancerListenerBackendServers(String loadBalancerId) {
        IAcsClient client = aliyunCore.getMasterClient();
        DescribeHealthStatusRequest request = new DescribeHealthStatusRequest();
        request.setLoadBalancerId(loadBalancerId);
        try {
            DescribeHealthStatusResponse response = client.getAcsResponse(request);
            return response.getBackendServers();
        } catch (ClientException e) {
            log.error("AliyunSLBHandler.queryLoadBalancerListenerBackendServers() 失败", e);
        }
        return Collections.emptyList();
    }
}
