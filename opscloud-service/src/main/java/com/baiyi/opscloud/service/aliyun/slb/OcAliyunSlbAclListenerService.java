package com.baiyi.opscloud.service.aliyun.slb;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclListener;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 2:37 下午
 * @Since 1.0
 */
public interface OcAliyunSlbAclListenerService {

    void addOcAliyunSlbAclListenerList(List<OcAliyunSlbAclListener> ocAliyunSlbAclListenerList);

    void deleteOcAliyunSlbAclListenerBySlbAclId(String slbAclId);

    OcAliyunSlbAclListener queryOcAliyunSlbAclListenerByLoadBalancerIdAndListenerPort(String loadBalancerId, Integer slbAclListenerPort);

    List<OcAliyunSlbAclListener> queryOcAliyunSlbAclListenerBySlbAclId(String slbAclId);
}
