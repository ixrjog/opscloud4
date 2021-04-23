package com.baiyi.opscloud.facade.aliyun.impl;

import com.baiyi.opscloud.cloud.dns.AliyunDNSCenter;
import com.baiyi.opscloud.convert.aliyun.dns.AliyunDNSRecordConvert;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomainRecord;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainRecordParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunDNSVO;
import com.baiyi.opscloud.facade.aliyun.AliyunDNSFacade;
import com.baiyi.opscloud.service.aliyun.dns.OcAliyunDomainRecordService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:59 下午
 * @Since 1.0
 */

@Component("AliyunDNSFacade")
public class AliyunDNSFacadeImpl implements AliyunDNSFacade {

    @Resource
    private AliyunDNSCenter aliyunDNSCenter;

    @Resource
    private OcAliyunDomainRecordService ocAliyunDomainRecordService;

    @Override
    public BusinessWrapper<Boolean> syncAliyunDomainRecord() {
        return aliyunDNSCenter.syncAliyunDomainRecord();
    }

    @Override
    public BusinessWrapper<Boolean> syncAliyunDomainRecordByName(String domainName) {
        return aliyunDNSCenter.syncAliyunDomainRecordByName(domainName);
    }

    @Override
    public BusinessWrapper<Boolean> addAliyunDomainRecord(AliyunDomainRecordParam.AddDomainRecord param) {
        return aliyunDNSCenter.addAliyunDomainRecord(param);
    }

    @Override
    public BusinessWrapper<Boolean> updateDomainRecord(AliyunDomainRecordParam.UpdateDomainRecord param) {
        return aliyunDNSCenter.updateDomainRecord(param);
    }

    @Override
    public BusinessWrapper<Boolean> domainRecordStatusUpdate(AliyunDomainRecordParam.SetDomainRecordStatus param) {
        return aliyunDNSCenter.domainRecordStatusUpdate(param);
    }

    @Override
    public BusinessWrapper<Boolean> delDomainRecord(String recordId) {
        return aliyunDNSCenter.delDomainRecord(recordId);
    }

    @Override
    public DataTable<AliyunDNSVO.Record> queryAliyunDNSPage(AliyunDomainRecordParam.PageQuery pageQuery) {
        DataTable<OcAliyunDomainRecord> table = ocAliyunDomainRecordService.queryOcAliyunDomainRecordPage(pageQuery);
        List<AliyunDNSVO.Record> recordList = AliyunDNSRecordConvert.toVOList(table.getData());
        return new DataTable<>(recordList, table.getTotalNum());
    }
}
