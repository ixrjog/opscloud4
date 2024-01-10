package com.baiyi.opscloud.datasource.aliyun.domain.driver;

import com.aliyuncs.domain.model.v20180129.QueryDomainListRequest;
import com.aliyuncs.domain.model.v20180129.QueryDomainListResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.core.AliyunClient;
import com.baiyi.opscloud.datasource.aliyun.domain.entity.AliyunDomain;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/4/18 11:46
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunDomainDriver {

    public interface Query {
        int PAGE_SIZE = 10;
    }

    private final AliyunClient aliyunClient;

    /**
     * 查询所有域名
     *
     * @param regionId
     * @param aliyun
     * @return
     */
    public List<AliyunDomain.Domain> listDomains(String regionId, AliyunConfig.Aliyun aliyun) throws ClientException {
        List<QueryDomainListResponse.Domain> domains = Lists.newArrayList();
        QueryDomainListRequest request = new QueryDomainListRequest();
        request.setPageNum(1);
        request.setPageSize(Query.PAGE_SIZE);
        while (true) {
            QueryDomainListResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            domains.addAll(response.getData());
            if (response.getNextPage()) {
                request.setPageNum(request.getPageNum() + 1);
            } else {
                break;
            }
        }
        return BeanCopierUtil.copyListProperties(domains, AliyunDomain.Domain.class);
    }

}