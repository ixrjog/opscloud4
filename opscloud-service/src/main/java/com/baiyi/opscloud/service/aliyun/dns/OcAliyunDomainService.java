package com.baiyi.opscloud.service.aliyun.dns;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunDomain;
import com.baiyi.opscloud.domain.param.cloud.AliyunDomainParam;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 2:43 下午
 * @Since 1.0
 */
public interface OcAliyunDomainService {

    OcAliyunDomain queryAliyunDomainById(Integer id);

    void addAliyunDomain(OcAliyunDomain ocAliyunDomain);

    void updateAliyunDomain(OcAliyunDomain ocAliyunDomain);

    void deleteAliyunDomain(int id);

    OcAliyunDomain queryAliyunDomainByInstanceId(String instanceId);

    List<OcAliyunDomain> queryAliyunDomainAll();

    DataTable<OcAliyunDomain> queryAliyunDomainPage(AliyunDomainParam.PageQuery pageQuery);
}
