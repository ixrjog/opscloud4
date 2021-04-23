package com.baiyi.opscloud.cloud.slb.builder;

import com.aliyuncs.slb.model.v20140515.DescribeAccessControlListAttributeResponse;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclEntry;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 3:29 下午
 * @Since 1.0
 */
public class OcAliyunSlbAclEntryBuilder {

    private static OcAliyunSlbAclEntry build(String slbAclId, DescribeAccessControlListAttributeResponse.AclEntry aclEntry) {
        OcAliyunSlbAclEntry ocAliyunSlbAclEntry = new OcAliyunSlbAclEntry();
        ocAliyunSlbAclEntry.setSlbAclId(slbAclId);
        ocAliyunSlbAclEntry.setSlbAclEntryIp(aclEntry.getAclEntryIP());
        ocAliyunSlbAclEntry.setSlbAclEntryComment(aclEntry.getAclEntryComment());
        return ocAliyunSlbAclEntry;
    }

    public static List<OcAliyunSlbAclEntry> buildList(String slbAclId, List<DescribeAccessControlListAttributeResponse.AclEntry> aclEntryList) {
        List<OcAliyunSlbAclEntry> ocAliyunSlbAclEntryList = Lists.newArrayListWithCapacity(aclEntryList.size());
        aclEntryList.forEach(aclListen -> ocAliyunSlbAclEntryList.add(build(slbAclId, aclListen)));
        return ocAliyunSlbAclEntryList;
    }
}
