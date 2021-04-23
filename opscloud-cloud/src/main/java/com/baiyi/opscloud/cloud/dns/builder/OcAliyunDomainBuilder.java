package com.baiyi.opscloud.cloud.dns.builder;

import com.aliyuncs.domain.model.v20180129.QueryDomainListResponse;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomain;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:40 下午
 * @Since 1.0
 */
public class OcAliyunDomainBuilder {

    public static OcAliyunDomain build(QueryDomainListResponse.Domain domain) {
        OcAliyunDomain ocAliyunDomain = new OcAliyunDomain();
        ocAliyunDomain.setDomainName(domain.getDomainName());
        ocAliyunDomain.setInstanceId(domain.getInstanceId());
        ocAliyunDomain.setDomainAuditStatus(domain.getDomainAuditStatus());
        ocAliyunDomain.setDomainGroupId(domain.getDomainGroupId());
        ocAliyunDomain.setDomainGroupName(domain.getDomainGroupName());
        ocAliyunDomain.setDomainStatus(domain.getDomainStatus());
        ocAliyunDomain.setDomainType(domain.getDomainType());
        ocAliyunDomain.setExpirationDate(domain.getExpirationDate());
        ocAliyunDomain.setProductId(domain.getProductId());
        ocAliyunDomain.setRegistrantType(domain.getRegistrantType());
        ocAliyunDomain.setRegistrationDate(domain.getRegistrationDate());
        ocAliyunDomain.setRemark(domain.getRemark());
        ocAliyunDomain.setIsActive(0);
        return ocAliyunDomain;
    }
}
