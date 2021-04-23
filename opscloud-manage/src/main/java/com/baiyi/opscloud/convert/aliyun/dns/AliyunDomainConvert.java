package com.baiyi.opscloud.convert.aliyun.dns;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomain;
import com.baiyi.opscloud.domain.vo.cloud.AliyunDomainVO;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 4:30 下午
 * @Since 1.0
 */
public class AliyunDomainConvert {

    private static AliyunDomainVO.Domain toVO(OcAliyunDomain ocAliyunDomain) {
        AliyunDomainVO.Domain domain = BeanCopierUtils.copyProperties(ocAliyunDomain, AliyunDomainVO.Domain.class);
        String expirationDate = ocAliyunDomain.getExpirationDate();
        Integer expirationCurrDateDiff = TimeUtils.calculateDateDiff4Day(new Date(), TimeUtils.gmtToDate(expirationDate));
        domain.setExpirationCurrDateDiff(expirationCurrDateDiff);
        String expirationDateStatus = TimeUtils.calculateDateExpired(expirationDate) ? "2" : "1";
        domain.setExpirationDateStatus(expirationDateStatus);
        return domain;
    }

    public static List<AliyunDomainVO.Domain> toVOList(List<OcAliyunDomain> ocAliyunDomainList) {
        List<AliyunDomainVO.Domain> domainList = Lists.newArrayListWithCapacity(ocAliyunDomainList.size());
        ocAliyunDomainList.forEach(vcAliyunDomain -> domainList.add(toVO(vcAliyunDomain)));
        return domainList;
    }
}
