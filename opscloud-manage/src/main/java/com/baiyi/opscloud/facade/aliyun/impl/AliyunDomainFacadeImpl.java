package com.baiyi.opscloud.facade.aliyun.impl;

import com.baiyi.opscloud.cloud.dns.AliyunDomainCenter;
import com.baiyi.opscloud.convert.aliyun.dns.AliyunDomainConvert;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomain;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainParam;
import com.baiyi.opscloud.domain.vo.cloud.AliyunDomainVO;
import com.baiyi.opscloud.facade.aliyun.AliyunDomainFacade;
import com.baiyi.opscloud.service.aliyun.dns.OcAliyunDomainService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:59 下午
 * @Since 1.0
 */

@Component("AliyunDomainFacade")
public class AliyunDomainFacadeImpl implements AliyunDomainFacade {

    @Resource
    private AliyunDomainCenter aliyunDomainCenter;

    @Resource
    private OcAliyunDomainService ocAliyunDomainService;

    @Override
    public BusinessWrapper<Boolean> syncAliyunDomain() {
        return new BusinessWrapper<>(aliyunDomainCenter.syncAliyunDomain());
    }

    @Override
    public BusinessWrapper<Boolean> setAliyunDomainActive(Integer id) {
        OcAliyunDomain ocAliyunDomain = ocAliyunDomainService.queryAliyunDomainById(id);
        ocAliyunDomain.setIsActive(ocAliyunDomain.getIsActive().equals(0) ? 1 : 0);
        ocAliyunDomainService.updateAliyunDomain(ocAliyunDomain);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<AliyunDomainVO.Domain> queryAliyunDomainPage(AliyunDomainParam.PageQuery pageQuery) {
        DataTable<OcAliyunDomain> table = ocAliyunDomainService.queryAliyunDomainPage(pageQuery);
        List<AliyunDomainVO.Domain> domainList = AliyunDomainConvert.toVOList(table.getData());
        return new DataTable<>(domainList, table.getTotalNum());
    }
}
