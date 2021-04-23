package com.baiyi.opscloud.aliyun.slb.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.slb.model.v20140515.DescribeServerCertificatesRequest;
import com.aliyuncs.slb.model.v20140515.DescribeServerCertificatesResponse;
import com.aliyuncs.slb.model.v20140515.SetDomainExtensionAttributeRequest;
import com.aliyuncs.slb.model.v20140515.SetLoadBalancerHTTPSListenerAttributeRequest;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.domain.param.cloud.AliyunSLBParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
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
@Component("AliyunSLBSCHandler")
public class AliyunSLBSCHandler {

    @Resource
    private AliyunCore aliyunCore;

    public List<DescribeServerCertificatesResponse.ServerCertificate> queryServerCertificateList() {
        DescribeServerCertificatesRequest request = new DescribeServerCertificatesRequest();
        request.setSysRegionId(aliyunCore.getAccount().getRegionId());
        IAcsClient client = aliyunCore.getMasterClient();
        try {
            DescribeServerCertificatesResponse response = client.getAcsResponse(request);
            if (!Strings.isEmpty(response.getRequestId()))
                return response.getServerCertificates();
        } catch (ClientException e) {
            log.error("AliyunSLBSCHandler.queryServerCertificateList() 失败", e);
        }
        return Collections.emptyList();
    }

    public Boolean replaceDefaultServerCertificate(AliyunSLBParam.ReplaceSC param) {
        IAcsClient client = aliyunCore.getMasterClient();
        SetLoadBalancerHTTPSListenerAttributeRequest request = new SetLoadBalancerHTTPSListenerAttributeRequest();
        request.setListenerPort(param.getListenerPort());
        request.setLoadBalancerId(param.getLoadBalancerId());
        request.setServerCertificateId(param.getUpdateServerCertificateId());
        try {
            client.getAcsResponse(request);
            return true;
        } catch (ClientException e) {
            log.error("AliyunSLBSCHandler.replaceDefaultServerCertificate() 失败", e);
        }
        return false;
    }

    public Boolean replaceExtensionServerCertificate(AliyunSLBParam.ReplaceSC param) {
        IAcsClient client = aliyunCore.getMasterClient();
        SetDomainExtensionAttributeRequest request = new SetDomainExtensionAttributeRequest();
        request.setDomainExtensionId(param.getDomainExtensionId());
        request.setServerCertificateId(param.getUpdateServerCertificateId());
        try {
            client.getAcsResponse(request);
            return true;
        } catch (ClientException e) {
            log.error("AliyunSLBSCHandler.replaceExtensionServerCertificate() 失败", e);
        }
        return false;
    }
}
