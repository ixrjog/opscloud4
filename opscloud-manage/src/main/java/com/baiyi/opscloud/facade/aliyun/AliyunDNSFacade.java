package com.baiyi.opscloud.facade.aliyun;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainRecordParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunDNSVO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:59 下午
 * @Since 1.0
 */
public interface AliyunDNSFacade {

    BusinessWrapper<Boolean> syncAliyunDomainRecord();

    BusinessWrapper<Boolean> syncAliyunDomainRecordByName(String domainName);

    BusinessWrapper<Boolean> addAliyunDomainRecord(AliyunDomainRecordParam.AddDomainRecord param);

    BusinessWrapper<Boolean> updateDomainRecord(AliyunDomainRecordParam.UpdateDomainRecord param);

    BusinessWrapper<Boolean> domainRecordStatusUpdate(AliyunDomainRecordParam.SetDomainRecordStatus param);

    BusinessWrapper<Boolean> delDomainRecord(String recordId);

    DataTable<AliyunDNSVO.Record> queryAliyunDNSPage(AliyunDomainRecordParam.PageQuery pageQuery);

}
