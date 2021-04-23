package com.baiyi.opscloud.aliyun.dns.impl;

import com.aliyuncs.domain.model.v20180129.QueryDomainListResponse;
import com.baiyi.opscloud.aliyun.dns.AliyunDomain;
import com.baiyi.opscloud.aliyun.dns.handler.AliyunDomainHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:07 下午
 * @Since 1.0
 */

@Component("AliyunDomain")
public class AliyunDomainImpl implements AliyunDomain {

    @Resource
    private AliyunDomainHandler aliyunDomainHandler;

    @Override
    public List<QueryDomainListResponse.Domain> queryDomainList() {
        return aliyunDomainHandler.queryDomainList();
    }
}
