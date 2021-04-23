package com.baiyi.opscloud.service.fault;

import com.baiyi.opscloud.domain.generator.opscloud.OcFaultResponsible;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 3:51 下午
 * @Since 1.0
 */
public interface OcFaultResponsibleService {

    void addOcFaultResponsibleList(List<OcFaultResponsible> ocFaultResponsibleList);

    void delOcFaultResponsibleByFaultId(Integer faultId);

    void updateOcFaultResponsible(OcFaultResponsible ocFaultResponsible);

    List<OcFaultResponsible> queryOcFaultResponsibleByFaultId(Integer faultId);
}
