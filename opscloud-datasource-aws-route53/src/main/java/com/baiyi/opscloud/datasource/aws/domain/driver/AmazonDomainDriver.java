package com.baiyi.opscloud.datasource.aws.domain.driver;

import com.amazonaws.services.route53domains.AmazonRoute53Domains;
import com.amazonaws.services.route53domains.model.DomainSummary;
import com.amazonaws.services.route53domains.model.ListDomainsRequest;
import com.amazonaws.services.route53domains.model.ListDomainsResult;
import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aws.domain.entity.AmazonDomain;
import com.baiyi.opscloud.datasource.aws.domain.service.AmazonRoute53DomainsService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/4/18 16:10
 * @Version 1.0
 */
public class AmazonDomainDriver {

    private static final String REGION_ID = "us-east-1";

    public static List<AmazonDomain.Domain> listDomains(AwsConfig.Aws config) {
        ListDomainsRequest request = new ListDomainsRequest();
        List<DomainSummary> domainSummaries = Lists.newArrayList();
        while (true) {
            ListDomainsResult result = buildAmazonRoute53Domains(config).listDomains(request);
            domainSummaries.addAll(result.getDomains());
            if (!StringUtils.isEmpty(result.getNextPageMarker())) {
                request.setMarker(result.getNextPageMarker());
            } else {
                break;
            }
        }
        return BeanCopierUtil.copyListProperties(domainSummaries, AmazonDomain.Domain.class);
    }

    private static AmazonRoute53Domains buildAmazonRoute53Domains(AwsConfig.Aws aws) {
        return AmazonRoute53DomainsService.buildAmazonRoute53Domains(aws, REGION_ID);
    }

}
