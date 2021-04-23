package com.baiyi.opscloud.aliyun.dns.impl;

import com.aliyuncs.alidns.model.v20150109.AddDomainRecordResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.baiyi.opscloud.aliyun.dns.AliyunDNS;
import com.baiyi.opscloud.aliyun.dns.handler.AliyunDNSHandler;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainRecordParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:07 下午
 * @Since 1.0
 */

@Component("AliyunDNS")
public class AliyunDNSImpl implements AliyunDNS {

    @Resource
    private AliyunDNSHandler aliyunDNSHandler;

    @Override
    public List<DescribeDomainRecordsResponse.Record> queryDomainRecordList(String domainName) {
        return aliyunDNSHandler.queryDomainRecordList(domainName);
    }

    @Override
    public AddDomainRecordResponse addDomainRecord(AliyunDomainRecordParam.AddDomainRecord param) {
        return aliyunDNSHandler.addDomainRecord(param);
    }

    @Override
    public Boolean updateDomainRecord(AliyunDomainRecordParam.UpdateDomainRecord param) {
        return aliyunDNSHandler.updateDomainRecord(param);
    }

    @Override
    public Boolean domainRecordStatusUpdate(AliyunDomainRecordParam.SetDomainRecordStatus param) {
        return aliyunDNSHandler.domainRecordStatusUpdate(param);
    }

    @Override
    public Boolean delDomainRecord(String recordId) {
        return aliyunDNSHandler.delDomainRecord(recordId);
    }
}
