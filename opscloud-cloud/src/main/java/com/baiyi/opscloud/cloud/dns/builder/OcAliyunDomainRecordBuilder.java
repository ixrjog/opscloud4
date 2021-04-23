package com.baiyi.opscloud.cloud.dns.builder;

import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomainRecord;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainRecordParam;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:46 下午
 * @Since 1.0
 */
public class OcAliyunDomainRecordBuilder {

    public static OcAliyunDomainRecord build(DescribeDomainRecordsResponse.Record record) {
        OcAliyunDomainRecord ocAliyunDomainRecord = new OcAliyunDomainRecord();
        ocAliyunDomainRecord.setDomainName(record.getDomainName());
        ocAliyunDomainRecord.setRecordId(record.getRecordId());
        ocAliyunDomainRecord.setRecordRr(record.getRR());
        ocAliyunDomainRecord.setRecordType(record.getType());
        ocAliyunDomainRecord.setRecordValue(record.getValue());
        ocAliyunDomainRecord.setRecordTtl(record.getTTL());
        ocAliyunDomainRecord.setRecordPriority(record.getPriority());
        ocAliyunDomainRecord.setRecordLine(record.getLine());
        ocAliyunDomainRecord.setRecordStatus(record.getStatus());
        ocAliyunDomainRecord.setRecordLocked(String.valueOf(record.getLocked()));
        ocAliyunDomainRecord.setRecordWeight(record.getWeight());
        ocAliyunDomainRecord.setRemark(record.getRemark());
        return ocAliyunDomainRecord;
    }

    public static OcAliyunDomainRecord build(AliyunDomainRecordParam.AddDomainRecord param, String recordId) {
        OcAliyunDomainRecord ocAliyunDomainRecord = new OcAliyunDomainRecord();
        ocAliyunDomainRecord.setDomainName(param.getDomainName());
        ocAliyunDomainRecord.setRecordId(recordId);
        ocAliyunDomainRecord.setRecordRr(param.getRecordRr());
        ocAliyunDomainRecord.setRecordType(param.getRecordType());
        ocAliyunDomainRecord.setRecordValue(param.getRecordValue());
        ocAliyunDomainRecord.setRecordTtl(600L);
        ocAliyunDomainRecord.setRecordLine("default");
        ocAliyunDomainRecord.setRecordStatus("ENABLE");
        ocAliyunDomainRecord.setRecordLocked("false");
        return ocAliyunDomainRecord;
    }
}
