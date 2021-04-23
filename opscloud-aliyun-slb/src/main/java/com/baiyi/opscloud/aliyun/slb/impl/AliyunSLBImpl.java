package com.baiyi.opscloud.aliyun.slb.impl;

import com.aliyuncs.slb.model.v20140515.DescribeHealthStatusResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancerAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancerHTTPSListenerAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersResponse;
import com.baiyi.opscloud.aliyun.slb.AliyunSLB;
import com.baiyi.opscloud.aliyun.slb.handler.AliyunSLBHandler;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 11:32 上午
 * @Since 1.0
 */

@Component("AliyunSLB")
public class AliyunSLBImpl implements AliyunSLB {

    @Resource
    private AliyunSLBHandler aliyunSLBHandler;

    @Override
    public List<DescribeLoadBalancersResponse.LoadBalancer> queryAliyunSLBList() {
        return aliyunSLBHandler.queryLoadBalancerList();
    }

    @Override
    public List<DescribeLoadBalancerAttributeResponse.ListenerPortAndProtocol> queryLoadBalancerListener(String loadBalancerId) {
        return aliyunSLBHandler.queryLoadBalancerListener(loadBalancerId);
    }

    @Override
    public DescribeLoadBalancerHTTPSListenerAttributeResponse queryHttpsListenerDetail(AliyunSLBParam.HttpsListenerQuery param) {
        return aliyunSLBHandler.queryHttpsListenerDetail(param);
    }

    @Override
    public List<DescribeHealthStatusResponse.BackendServer> queryLoadBalancerListenerBackendServers(String loadBalancerId) {
        return aliyunSLBHandler.queryLoadBalancerListenerBackendServers(loadBalancerId);
    }
}
