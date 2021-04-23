package com.baiyi.opscloud.cloud.slb.builder;

import com.aliyuncs.slb.model.v20140515.DescribeLoadBalancerHTTPSListenerAttributeResponse;
import com.baiyi.opscloud.common.base.AliyunSLBSCType;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbHttpsListener;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:25 下午
 * @Since 1.0
 */
public class OcAliyunSLBHttpsListenerBuilder {

    public static List<OcAliyunSlbHttpsListener> build(
            AliyunSLBParam.HttpsListenerQuery param, DescribeLoadBalancerHTTPSListenerAttributeResponse response, String domain, String loadBalancerName) {
        List<OcAliyunSlbHttpsListener> httpsListenerList = Lists.newArrayList();
        OcAliyunSlbHttpsListener defaultHttpsListener = new OcAliyunSlbHttpsListener();
        defaultHttpsListener.setLoadBalancerId(param.getLoadBalancerId());
        defaultHttpsListener.setLoadBalancerName(loadBalancerName);
        defaultHttpsListener.setHttpsListenerPort(param.getHttpsListenerPort());
        defaultHttpsListener.setServerCertificateId(response.getServerCertificateId());
        defaultHttpsListener.setServerCertificateDomain(domain);
        defaultHttpsListener.setServerCertificateType(AliyunSLBSCType.DEFAULT.getCode());
        httpsListenerList.add(defaultHttpsListener);
        if (!CollectionUtils.isEmpty(response.getDomainExtensions())) {
            response.getDomainExtensions().forEach(extension -> {
                OcAliyunSlbHttpsListener extensionHttpsListener = new OcAliyunSlbHttpsListener();
                extensionHttpsListener.setLoadBalancerId(param.getLoadBalancerId());
                extensionHttpsListener.setLoadBalancerName(loadBalancerName);
                extensionHttpsListener.setHttpsListenerPort(param.getHttpsListenerPort());
                extensionHttpsListener.setServerCertificateId(extension.getServerCertificateId());
                extensionHttpsListener.setServerCertificateDomain(extension.getDomain());
                extensionHttpsListener.setDomainExtensionId(extension.getDomainExtensionId());
                extensionHttpsListener.setServerCertificateType(AliyunSLBSCType.EXTENSION.getCode());
                httpsListenerList.add(extensionHttpsListener);
            });
        }
        return httpsListenerList;
    }
}
