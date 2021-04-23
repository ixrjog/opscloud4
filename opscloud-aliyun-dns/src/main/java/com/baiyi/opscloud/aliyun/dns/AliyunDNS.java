package com.baiyi.opscloud.aliyun.dns;

import com.aliyuncs.alidns.model.v20150109.AddDomainRecordResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainRecordParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:06 下午
 * @Since 1.0
 */
public interface AliyunDNS {

    List<DescribeDomainRecordsResponse.Record> queryDomainRecordList(String domainName);

    AddDomainRecordResponse addDomainRecord(AliyunDomainRecordParam.AddDomainRecord param);

    Boolean updateDomainRecord(AliyunDomainRecordParam.UpdateDomainRecord param);

    Boolean domainRecordStatusUpdate(AliyunDomainRecordParam.SetDomainRecordStatus param);

    Boolean delDomainRecord(String recordId);
}
