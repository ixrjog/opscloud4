package com.baiyi.opscloud.aliyun.dns;

import com.aliyuncs.domain.model.v20180129.QueryDomainListResponse;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:06 下午
 * @Since 1.0
 */
public interface AliyunDomain {

    List<QueryDomainListResponse.Domain> queryDomainList();
}
