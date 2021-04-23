package com.baiyi.opscloud.service.aliyun.slb;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunSlbAclEntry;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 2:36 下午
 * @Since 1.0
 */
public interface OcAliyunSlbAclEntryService {

    void addOcAliyunSlbAclEntryList(List<OcAliyunSlbAclEntry> ocAliyunSlbAclEntryList);

    void deleteOcAliyunSlbAclEntryBySlbAclId(String slbAclId);

    List<OcAliyunSlbAclEntry> queryOcAliyunSlbAclEntryBySlbAclId(String slbAclId);
}
