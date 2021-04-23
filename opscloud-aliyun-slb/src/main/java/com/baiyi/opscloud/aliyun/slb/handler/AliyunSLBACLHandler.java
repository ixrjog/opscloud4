package com.baiyi.opscloud.aliyun.slb.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListAttributeRequest;
import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListsRequest;
import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListsResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 11:15 上午
 * @Since 1.0
 */

@Slf4j
@Component("AliyunSLBACLHandler")
public class AliyunSLBACLHandler {

    @Resource
    private AliyunCore aliyunCore;

    public List<DescribeAccessControlListsResponse.Acl> queryLoadBalancerACLList() {
        int pageSize = 10;
        DescribeAccessControlListsRequest describe = new DescribeAccessControlListsRequest();
        describe.setPageSize(pageSize);
        DescribeAccessControlListsResponse response = getDescribeAccessControlListsResponse(describe);
        assert response != null;
        List<DescribeAccessControlListsResponse.Acl> aclList = Lists.newArrayList(response.getAcls());
        // 获取总数
        int totalCount = response.getTotalCount();
        // 循环次数
        int cnt = (totalCount + pageSize - 1) / pageSize;
        for (int i = 1; i < cnt; i++) {
            describe.setPageNumber(i + 1);
            response = getDescribeAccessControlListsResponse(describe);
            aclList.addAll(response.getAcls());
        }
        return aclList;
    }

    private DescribeAccessControlListsResponse getDescribeAccessControlListsResponse(DescribeAccessControlListsRequest request) {
        IAcsClient client = aliyunCore.getMasterClient();
        try {
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("AliyunSLBACLHandler.getDescribeAccessControlListsResponse()失败", e);
        }
        return null;
    }

    public DescribeAccessControlListAttributeResponse queryAccessControlDetail(String aclId) {
        IAcsClient client = aliyunCore.getMasterClient();
        DescribeAccessControlListAttributeRequest request = new DescribeAccessControlListAttributeRequest();
        request.setAclId(aclId);
        try {
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("AliyunSLBACLHandler.queryAccessControlDetail()失败", e);
        }
        return null;
    }
}
