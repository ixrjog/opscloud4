package com.baiyi.opscloud.factory.change.consumer.impl;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.factory.change.consumer.IServerChangeConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2020/5/29 9:33 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseServerChangeConsumer implements IServerChangeConsumer, InitializingBean {

    @Override
    public BusinessWrapper<Boolean> consuming(OcServerChangeTask ocServerChangeTask, OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        return BusinessWrapper.SUCCESS;
    }

}
