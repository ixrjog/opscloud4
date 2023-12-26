package com.baiyi.opscloud.leo.handler.deploy;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.handler.deploy.chain.BootDeployChainHandler;
import com.baiyi.opscloud.leo.handler.deploy.chain.pre.DeployingSupervisorChainHandler;
import com.baiyi.opscloud.leo.handler.deploy.chain.pre.DoDeployChainHandler;
import com.baiyi.opscloud.leo.handler.deploy.chain.pre.PreInspectionChainHandler;
import com.baiyi.opscloud.leo.handler.deploy.chain.pre.StartDeployNotificationChainHandler;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/12/5 19:53
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoDeployHandler implements InitializingBean {

    /**
     * 启动
     */
    @Resource
    private BootDeployChainHandler bootHandler;

    /**
     * 前置检查
     */
    @Resource
    private PreInspectionChainHandler preInspectionChainHandler;

    /**
     * 执行部署
     */
    @Resource
    private DoDeployChainHandler doDeployChainHandler;

    /**
     * 启动部署通知
     */
    @Resource
    private StartDeployNotificationChainHandler startDeployNotificationChainHandler;

    /**
     * 启动部署监视器
     */
    @Resource
    private DeployingSupervisorChainHandler deployingSupervisorChainHandler;

    @Async
    public void handleDeploy(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        /*
         * 使用责任链设计模式解耦代码
         */
        bootHandler.handleRequest(leoDeploy, deployConfig);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /*
         * 装配
         */
        bootHandler.setNextHandler(preInspectionChainHandler)
                .setNextHandler(doDeployChainHandler)
                .setNextHandler(startDeployNotificationChainHandler)
                .setNextHandler(deployingSupervisorChainHandler);
    }

}