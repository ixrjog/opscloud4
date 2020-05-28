package com.baiyi.opscloud.service.serverChange;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;

/**
 * @Author baiyi
 * @Date 2020/5/28 1:24 下午
 * @Version 1.0
 */
public interface OcServerChangeTaskFlowService {

    void addOcServerChangeTaskFlow(OcServerChangeTaskFlow ocServerChangeTaskFlow);

    void updateOcServerChangeTaskFlow(OcServerChangeTaskFlow ocServerChangeTaskFlow);
}
