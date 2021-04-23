package com.baiyi.opscloud.cloud.slb.builder;

import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListsResponse;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclListener;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:30 下午
 * @Since 1.0
 */
public class OcAliyunSlbAclListenerBuilder {

    private static OcAliyunSlbAclListener build(
            DescribeAccessControlListsResponse.Acl acl,
            DescribeAccessControlListAttributeResponse.RelatedListener listener) {
        OcAliyunSlbAclListener ocAliyunSlbAclListener = new OcAliyunSlbAclListener();
        ocAliyunSlbAclListener.setSlbAclId(acl.getAclId());
        ocAliyunSlbAclListener.setSlbAclName(acl.getAclName());
        ocAliyunSlbAclListener.setSlbAclType(listener.getAclType());
        ocAliyunSlbAclListener.setSlbAclListenerPort(listener.getListenerPort());
        ocAliyunSlbAclListener.setLoadBalancerId(listener.getLoadBalancerId());
        ocAliyunSlbAclListener.setSlbAclProtocol(listener.getBizProtocol());
        return ocAliyunSlbAclListener;
    }

    public static List<OcAliyunSlbAclListener> buildList(
            DescribeAccessControlListsResponse.Acl acl,
            List<DescribeAccessControlListAttributeResponse.RelatedListener> listenerList) {
        List<OcAliyunSlbAclListener> aclListenerArrayList = Lists.newArrayListWithCapacity(listenerList.size());
        listenerList.forEach(aclListen -> aclListenerArrayList.add(build(acl, aclListen)));
        return aclListenerArrayList;
    }
}
