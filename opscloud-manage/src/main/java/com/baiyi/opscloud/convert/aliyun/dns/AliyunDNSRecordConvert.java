package com.baiyi.opscloud.convert.aliyun.dns;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomainRecord;
import com.baiyi.opscloud.domain.vo.cloud.AliyunDNSVO;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 4:16 下午
 * @Since 1.0
 */
public class AliyunDNSRecordConvert {

    private static AliyunDNSVO.Record toVO(OcAliyunDomainRecord ocAliyunDomainRecord) {
        return BeanCopierUtils.copyProperties(ocAliyunDomainRecord, AliyunDNSVO.Record.class);
    }

    public static List<AliyunDNSVO.Record> toVOList(List<OcAliyunDomainRecord> ocAliyunDomainRecordList) {
        List<AliyunDNSVO.Record> recordList = Lists.newArrayListWithCapacity(ocAliyunDomainRecordList.size());
        ocAliyunDomainRecordList.forEach(vcAliyunDomainRecord -> recordList.add(toVO(vcAliyunDomainRecord)));
        return recordList;
    }
}
