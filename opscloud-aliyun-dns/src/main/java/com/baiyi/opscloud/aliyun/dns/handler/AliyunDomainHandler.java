package com.baiyi.opscloud.aliyun.dns.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.domain.model.v20180129.QueryDomainListRequest;
import com.aliyuncs.domain.model.v20180129.QueryDomainListResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 2:47 下午
 * @Since 1.0
 */
@Slf4j
@Component("AliyunDomainHandler")
public class AliyunDomainHandler {

    @Resource
    private AliyunCore aliyunCore;

    public List<QueryDomainListResponse.Domain> queryDomainList() {
        IAcsClient client = aliyunCore.getMasterClient();
        List<QueryDomainListResponse.Domain> domainList = Lists.newArrayList();
        boolean nextPage;
        int pageNum = 1;
        int pageSize = 10;
        try {
            do {
                QueryDomainListRequest request = new QueryDomainListRequest();
                request.setPageNum(pageNum);
                request.setPageSize(pageSize);
                QueryDomainListResponse response = client.getAcsResponse(request);
                List<QueryDomainListResponse.Domain> domains = response.getData();
                domainList.addAll(domains);
                nextPage = response.getNextPage();
                pageNum++;
            } while (nextPage);
            return domainList;
        } catch (ClientException e) {
            log.error("批量查询域名列表失败", e);
            return Collections.emptyList();
        }
    }
}
