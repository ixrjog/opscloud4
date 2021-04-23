package com.baiyi.opscloud.aliyun.slb;

import com.aliyuncs.slb.model.v20140515.DescribeHealthStatusResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancerAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancerHTTPSListenerAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancersResponse;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 11:31 上午
 * @Since 1.0
 */
public interface AliyunSLB {

    List<DescribeLoadBalancersResponse.LoadBalancer> queryAliyunSLBList();

    List<DescribeLoadBalancerAttributeResponse.ListenerPortAndProtocol> queryLoadBalancerListener(String loadBalancerId);

    DescribeLoadBalancerHTTPSListenerAttributeResponse queryHttpsListenerDetail(AliyunSLBParam.HttpsListenerQuery param);

    List<DescribeHealthStatusResponse.BackendServer> queryLoadBalancerListenerBackendServers(String loadBalancerId);
}
