package com.baiyi.opscloud.cloud.slb.builder;

import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListsResponse;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAcl;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:32 下午
 * @Since 1.0
 */
public class OcAliyunSLBCACLBuilder {

    public static OcAliyunSlbAcl build(DescribeAccessControlListsResponse.Acl acl) {
        OcAliyunSlbAcl ocAliyunSlbAcl = new OcAliyunSlbAcl();
        ocAliyunSlbAcl.setSlbAclId(acl.getAclId());
        ocAliyunSlbAcl.setSlbAclName(acl.getAclName());
        ocAliyunSlbAcl.setAddressIpVersion(acl.getAddressIPVersion());
        ocAliyunSlbAcl.setResourceGroupId(acl.getResourceGroupId());
        return ocAliyunSlbAcl;
    }
}
