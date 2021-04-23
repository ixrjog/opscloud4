package com.baiyi.opscloud.aliyun.dns.handler;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.*;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainRecordParam;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 2:47 下午
 * @Since 1.0
 */

@Slf4j
@Component("AliyunDNSHandler")
public class AliyunDNSHandler {

    @Resource
    private AliyunCore aliyunCore;

    public List<DescribeDomainRecordsResponse.Record> queryDomainRecordList(String domainName) {
        IAcsClient client = aliyunCore.getMasterClient();
        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
        request.setDomainName(domainName);
        List<DescribeDomainRecordsResponse.Record> recordList = Lists.newArrayList();
        int pageSize = 20;
        try {
            DescribeDomainRecordsResponse response = client.getAcsResponse(request);
            Long totalCount = response.getTotalCount();
            for (long pageNumber = 1; (pageNumber - 1) * pageSize < totalCount; pageNumber++) {
                DescribeDomainRecordsRequest acpRequest = new DescribeDomainRecordsRequest();
                acpRequest.setPageNumber(pageNumber);
                acpRequest.setDomainName(domainName);
                List<DescribeDomainRecordsResponse.Record> records = client.getAcsResponse(acpRequest).getDomainRecords();
                recordList.addAll(records);
            }
            return recordList;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return recordList;
    }

    public AddDomainRecordResponse addDomainRecord(AliyunDomainRecordParam.AddDomainRecord param) {
        IAcsClient client = aliyunCore.getMasterClient();
        AddDomainRecordRequest request = new AddDomainRecordRequest();
        request.setDomainName(param.getDomainName());
        request.setRR(param.getRecordRr());
        request.setType(param.getRecordType());
        request.setValue(param.getRecordValue());
        try {
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("新增域名解析失败", e);
        }
        return null;
    }

    public Boolean updateDomainRecord(AliyunDomainRecordParam.UpdateDomainRecord param) {
        IAcsClient client = aliyunCore.getMasterClient();
        UpdateDomainRecordRequest request = new UpdateDomainRecordRequest();
        request.setRecordId(param.getRecordId());
        request.setRR(param.getRecordRr());
        request.setType(param.getRecordType());
        request.setValue(param.getRecordValue());
        try {
            client.getAcsResponse(request);
            return true;
        } catch (ClientException e) {
            log.error("更新域名解析失败", e);
        }
        return false;
    }

    public Boolean domainRecordStatusUpdate(AliyunDomainRecordParam.SetDomainRecordStatus param) {
        IAcsClient client = aliyunCore.getMasterClient();
        SetDomainRecordStatusRequest request = new SetDomainRecordStatusRequest();
        request.setRecordId(param.getRecordId());
        request.setStatus(param.getRecordStatus());
        try {
            client.getAcsResponse(request);
            return true;
        } catch (ClientException e) {
            log.error("更新域名解析状态失败", e);
        }
        return false;
    }

    public Boolean delDomainRecord(String recordId) {
        IAcsClient client = aliyunCore.getMasterClient();
        DeleteDomainRecordRequest request = new DeleteDomainRecordRequest();
        request.setRecordId(recordId);
        try {
            client.getAcsResponse(request);
            return true;
        } catch (ClientException e) {
            log.error("删除域名解析失败", e);
        }
        return false;
    }
}
