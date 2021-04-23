package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorder;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/28 10:29 上午
 * @Version 1.0
 */
public interface OcWorkorderService {

    OcWorkorder queryOcWorkorderByWorkorderKey(String workorderKey);

    /**
     *
     * @param workorderGroupId
     * @param isDevelopment 开发模式显示所有状态的工单
     * @return
     */
    List<OcWorkorder> queryOcWorkorderByGroupId(int workorderGroupId, boolean isDevelopment);

    OcWorkorder queryOcWorkorderById(int id);

    void addOcWorkorder(OcWorkorder ocWorkorder);

    void updateOcWorkorder(OcWorkorder ocWorkorder);

    List<OcWorkorder> queryOcWorkorderAll();
}
