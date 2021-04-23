package com.baiyi.opscloud.cloud.slb;

import com.aliyuncs.slb.model.v20140515.DescribeHealthStatusResponse;
import com.baiyi.opscloud.domain.BusinessWrapper;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:13 下午
 * @Since 1.0
 */
public interface AliyunSLBCenter {

    BusinessWrapper<Boolean> syncSLB();

    BusinessWrapper<Boolean> refreshSLBListener(String loadBalancerId);

    List<DescribeHealthStatusResponse.BackendServer> querySLBListenerBackendServers(String loadBalancerId);
}
