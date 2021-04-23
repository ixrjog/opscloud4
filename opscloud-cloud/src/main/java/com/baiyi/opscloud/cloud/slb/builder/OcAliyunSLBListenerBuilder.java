package com.baiyi.opscloud.cloud.slb.builder;

import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancerAttributeResponse;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbListener;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:33 下午
 * @Since 1.0
 */
public class OcAliyunSLBListenerBuilder {

    private static OcAliyunSlbListener build(
            String loadBalancerId, DescribeLoadBalancerAttributeResponse.ListenerPortAndProtocol protocol) {
        OcAliyunSlbListener ocAliyunSlbListener = new OcAliyunSlbListener();
        ocAliyunSlbListener.setLoadBalancerId(loadBalancerId);
        ocAliyunSlbListener.setListenerPort(protocol.getListenerPort());
        ocAliyunSlbListener.setListenerProtocol(protocol.getListenerProtocol());
        ocAliyunSlbListener.setListenerForward(protocol.getListenerForward());
        ocAliyunSlbListener.setForwardPort(protocol.getForwardPort());
        ocAliyunSlbListener.setListenDescription(protocol.getDescription());
        return ocAliyunSlbListener;
    }

    public static List<OcAliyunSlbListener> buildList(
            String loadBalancerId, List<DescribeLoadBalancerAttributeResponse.ListenerPortAndProtocol> protocolList) {
        List<OcAliyunSlbListener> ocAliyunSlbListenerList = Lists.newArrayListWithCapacity(protocolList.size());
        protocolList.forEach(protocol -> ocAliyunSlbListenerList.add(build(loadBalancerId, protocol)));
        return ocAliyunSlbListenerList;
    }
}
