package com.baiyi.opscloud.aliyun.slb;

import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListAttributeResponse;
import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListsResponse;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 11:31 上午
 * @Since 1.0
 */
public interface AliyunSLBACL {

    List<DescribeAccessControlListsResponse.Acl> queryLoadBalancerACLList();

    DescribeAccessControlListAttributeResponse queryAccessControlDetail(String aclId);
}
