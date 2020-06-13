package com.baiyi.opscloud.factory.change.consumer;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;

/**
 * @Author baiyi
 * @Date 2020/5/29 9:33 上午
 * @Version 1.0
 */
public interface IServerChangeConsumer {

    String getKey();

    BusinessWrapper<Boolean> consuming(OcServerChangeTask ocServerChangeTask, OcServerChangeTaskFlow ocServerChangeTaskFlow);
}
