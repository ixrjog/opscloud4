package com.baiyi.opscloud.factory.change;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;

/**
 * @Author baiyi
 * @Date 2020/5/27 4:11 下午
 * @Version 1.0
 */
public interface IServerChange {

    String getKey();

    /**
     * 创建流程
     * @param ocServerChangeTask
     * @param ocServer
     * @return
     */
    BusinessWrapper<Boolean> createFlow(OcServerChangeTask ocServerChangeTask , OcServer ocServer);
}
