package com.baiyi.opscloud.factory.change.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTaskFlow;
import com.baiyi.opscloud.factory.change.IServerChange;
import com.baiyi.opscloud.factory.change.ServerChangeFactory;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/27 4:14 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseServerChange implements IServerChange, InitializingBean {

    @Resource
    private OcServerChangeTaskFlowService ocServerChangeTaskFlowService;

    protected void addOcServerChangeTaskFlow(OcServerChangeTaskFlow ocServerChangeTaskFlow) {
        ocServerChangeTaskFlowService.addOcServerChangeTaskFlow(ocServerChangeTaskFlow);
    }


    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        ServerChangeFactory.register(this);
    }
}
