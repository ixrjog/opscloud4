package com.baiyi.opscloud.convert.aliyun.slb;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclEntry;
import com.baiyi.opscloud.domain.vo.cloud.AliyunSLBVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 5:35 下午
 * @Since 1.0
 */
public class AliyunSLBAccessControlEntryConvert {

    public static AliyunSLBVO.AccessControlEntry toVO(OcAliyunSlbAclEntry ocAliyunSlbAclEntry) {
        return BeanCopierUtils.copyProperties(ocAliyunSlbAclEntry, AliyunSLBVO.AccessControlEntry.class);
    }

    public static List<AliyunSLBVO.AccessControlEntry> toVOList(List<OcAliyunSlbAclEntry> ocAliyunSlbAclEntryList) {
        List<AliyunSLBVO.AccessControlEntry> aclEntryList = Lists.newArrayListWithCapacity(ocAliyunSlbAclEntryList.size());
        ocAliyunSlbAclEntryList.forEach(aclEntry -> aclEntryList.add(toVO(aclEntry)));
        return aclEntryList;
    }
}
