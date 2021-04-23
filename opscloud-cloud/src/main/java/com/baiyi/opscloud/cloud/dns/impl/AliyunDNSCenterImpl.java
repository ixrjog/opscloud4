package com.baiyi.opscloud.cloud.dns.impl;

import com.aliyuncs.alidns.model.v20150109.AddDomainRecordResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.baiyi.opscloud.aliyun.dns.AliyunDNS;
import com.baiyi.opscloud.cloud.dns.AliyunDNSCenter;
import com.baiyi.opscloud.cloud.dns.builder.OcAliyunDomainRecordBuilder;
import com.baiyi.opscloud.common.util.IPUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomain;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomainRecord;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainRecordParam;
import com.baiyi.opscloud.service.aliyun.dns.OcAliyunDomainRecordService;
import com.baiyi.opscloud.service.aliyun.dns.OcAliyunDomainService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:36 下午
 * @Since 1.0
 */

@Component("AliyunDNSCenter")
public class AliyunDNSCenterImpl implements AliyunDNSCenter {

    @Resource
    private AliyunDNS aliyunDNS;

    @Resource
    private OcAliyunDomainRecordService ocAliyunDomainRecordService;

    @Resource
    private OcAliyunDomainService ocAliyunDomainService;

    @Override
    public BusinessWrapper<Boolean> syncAliyunDomainRecord() {
        List<OcAliyunDomain> domainList = ocAliyunDomainService.queryAliyunDomainAll();
        domainList.forEach(domain -> syncAliyunDomainRecordByName(domain.getDomainName()));
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> syncAliyunDomainRecordByName(String domainName) {
        HashMap<String, OcAliyunDomainRecord> map = getDomainRecordMap(domainName);
        List<DescribeDomainRecordsResponse.Record> recordList = aliyunDNS.queryDomainRecordList(domainName);
        recordList.forEach(record -> {
            saveDomainRecord(record);
            map.remove(record.getRecordId());
        });
        delDomainRecordByMap(map);
        return BusinessWrapper.SUCCESS;
    }

    private HashMap<String, OcAliyunDomainRecord> getDomainRecordMap(String domainName) {
        List<OcAliyunDomainRecord> ocAliyunDomainRecordList = ocAliyunDomainRecordService.queryAliyunDomainRecordByDomainName(domainName);
        HashMap<String, OcAliyunDomainRecord> map = Maps.newHashMap();
        ocAliyunDomainRecordList.forEach(record -> map.put(record.getRecordId(), record));
        return map;
    }

    private void saveDomainRecord(DescribeDomainRecordsResponse.Record record) {
        OcAliyunDomainRecord aliyunDomainRecord = ocAliyunDomainRecordService.queryAliyunDomainRecordByRecordId(record.getRecordId());
        OcAliyunDomainRecord newAliyunDomainRecord = OcAliyunDomainRecordBuilder.build(record);
        if (aliyunDomainRecord == null) {
            try {
                ocAliyunDomainRecordService.addOcAliyunDomainRecord(newAliyunDomainRecord);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newAliyunDomainRecord.setId(aliyunDomainRecord.getId());
            ocAliyunDomainRecordService.updateOcAliyunDomainRecord(newAliyunDomainRecord);
        }
    }

    private void delDomainRecordByMap(HashMap<String, OcAliyunDomainRecord> map) {
        map.forEach((key, value) -> ocAliyunDomainRecordService.deleteOcAliyunDomainRecord(value.getId()));
    }


    private Boolean checkParam(String recordType, String recordValue) {
        // todo 入参检验 https://help.aliyun.com/document_detail/29805.html?spm=a2c4g.11186623.2.15.237f2846KOUJik
        if (recordType.equals("A")) {
            return IPUtils.checkIp(recordValue);
        }
        return true;
    }

    @Override
    public BusinessWrapper<Boolean> addAliyunDomainRecord(AliyunDomainRecordParam.AddDomainRecord param) {
        Boolean checkResult = checkParam(param.getRecordType(), param.getRecordValue());
        if (!checkResult)
            return new BusinessWrapper<>(ErrorEnum.ALIYUN_DNS_PARAM_ERR);
        AddDomainRecordResponse result = aliyunDNS.addDomainRecord(param);
        if (result != null) {
            ocAliyunDomainRecordService.addOcAliyunDomainRecord(OcAliyunDomainRecordBuilder.build(param, result.getRecordId()));
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_DNS_ADD_FAIL);
    }

    @Override
    public BusinessWrapper<Boolean> updateDomainRecord(AliyunDomainRecordParam.UpdateDomainRecord param) {
        Boolean checkResult = checkParam(param.getRecordType(), param.getRecordValue());
        if (!checkResult)
            return new BusinessWrapper<>(ErrorEnum.ALIYUN_DNS_PARAM_ERR);
        Boolean result = aliyunDNS.updateDomainRecord(param);
        if (result) {
            OcAliyunDomainRecord aliyunDomainRecord = ocAliyunDomainRecordService.queryAliyunDomainRecordByRecordId(param.getRecordId());
            aliyunDomainRecord.setRecordRr(param.getRecordRr());
            aliyunDomainRecord.setRecordType(param.getRecordType());
            aliyunDomainRecord.setRecordValue(param.getRecordValue());
            ocAliyunDomainRecordService.updateOcAliyunDomainRecord(aliyunDomainRecord);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_DNS_UPDATE_FAIL);
    }

    @Override
    public BusinessWrapper<Boolean> domainRecordStatusUpdate(AliyunDomainRecordParam.SetDomainRecordStatus param) {
        param.setRecordStatus(param.getRecordStatus().equals("ENABLE") ? "DISABLE" : "ENABLE");
        Boolean result = aliyunDNS.domainRecordStatusUpdate(param);
        if (result) {
            OcAliyunDomainRecord aliyunDomainRecord = ocAliyunDomainRecordService.queryAliyunDomainRecordByRecordId(param.getRecordId());
            aliyunDomainRecord.setRecordStatus(param.getRecordStatus());
            ocAliyunDomainRecordService.updateOcAliyunDomainRecord(aliyunDomainRecord);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_DNS_UPDATE_FAIL);
    }

    @Override
    public BusinessWrapper<Boolean> delDomainRecord(String recordId) {
        Boolean result = aliyunDNS.delDomainRecord(recordId);
        if (result) {
            ocAliyunDomainRecordService.deleteOcAliyunDomainRecordByRecordId(recordId);
            return BusinessWrapper.SUCCESS;
        }
        return new BusinessWrapper<>(ErrorEnum.ALIYUN_DNS_DEL_FAIL);
    }

}
