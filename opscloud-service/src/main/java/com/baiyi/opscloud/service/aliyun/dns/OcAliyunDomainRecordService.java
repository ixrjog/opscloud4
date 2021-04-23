package com.baiyi.opscloud.service.aliyun.dns;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomainRecord;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainRecordParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 2:43 下午
 * @Since 1.0
 */
public interface OcAliyunDomainRecordService {

    OcAliyunDomainRecord queryOcAliyunDomainRecordById(Integer id);

    void addOcAliyunDomainRecord(OcAliyunDomainRecord ocAliyunDomainRecord);

    void updateOcAliyunDomainRecord(OcAliyunDomainRecord ocAliyunDomainRecord);

    void deleteOcAliyunDomainRecord(int id);

    void deleteOcAliyunDomainRecordByRecordId(String recordId);

    List<OcAliyunDomainRecord> queryAliyunDomainRecordByDomainName(String domainName);

    List<OcAliyunDomainRecord> queryAliyunDomainRecordByNameAndRr(String domainName, String recordRr);

    OcAliyunDomainRecord queryAliyunDomainRecordByRecordId(String recordId);

    DataTable<OcAliyunDomainRecord> queryOcAliyunDomainRecordPage(AliyunDomainRecordParam.PageQuery pageQuery);
}
