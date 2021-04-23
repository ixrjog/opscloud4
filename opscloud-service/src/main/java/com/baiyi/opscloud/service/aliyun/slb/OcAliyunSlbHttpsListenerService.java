package com.baiyi.opscloud.service.aliyun.slb;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbHttpsListener;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 2:37 下午
 * @Since 1.0
 */
public interface OcAliyunSlbHttpsListenerService {

    void addOcAliyunSlbHttpsListenerList(List<OcAliyunSlbHttpsListener> ocAliyunSlbHttpsListenerList);

    void updateOcAliyunSlbHttpsListener(OcAliyunSlbHttpsListener ocAliyunSlbHttpsListener);

    void deleteOcAliyunSlbHttpsListenerByHttpsListenerParam(AliyunSLBParam.HttpsListenerQuery param);

    void deleteOcAliyunSlbHttpsListenerByLoadBalancerId(String loadBalancerId);

    List<OcAliyunSlbHttpsListener> queryOcAliyunSlbHttpsListenerByHttpsListenerParam(AliyunSLBParam.HttpsListenerQuery param);

    OcAliyunSlbHttpsListener queryDefaultOcAliyunSlbHttpsListenerByHttpsListenerParam(AliyunSLBParam.HttpsListenerQuery param);

    OcAliyunSlbHttpsListener queryExtensionOcAliyunSlbHttpsListenerByDomainExtensionId(String domainExtensionId);

    List<OcAliyunSlbHttpsListener> queryOcAliyunSlbHttpsListenerBySCId(String serverCertificateId);

}
