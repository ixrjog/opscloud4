package com.baiyi.opscloud.aliyun.slb.impl;

import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListsResponse;
import com.baiyi.opscloud.aliyun.slb.AliyunSLBACL;
import com.baiyi.opscloud.aliyun.slb.handler.AliyunSLBACLHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 11:32 上午
 * @Since 1.0
 */
@Component("AliyunSLBACL")
public class AliyunSLBACLImpl implements AliyunSLBACL {

    @Resource
    private AliyunSLBACLHandler aliyunSLBACLHandler;

    @Override
    public List<DescribeAccessControlListsResponse.Acl> queryLoadBalancerACLList() {
        return aliyunSLBACLHandler.queryLoadBalancerACLList();
    }

    @Override
    public DescribeAccessControlListAttributeResponse queryAccessControlDetail(String aclId) {
        return aliyunSLBACLHandler.queryAccessControlDetail(aclId);
    }
}
