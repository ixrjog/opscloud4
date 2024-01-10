package com.baiyi.opscloud.leo.handler.deploy;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.handler.deploy.chain.post.DeployFinalProcessingChainHandler;
import com.baiyi.opscloud.leo.handler.deploy.chain.post.EndDeployNotificationChainHandler;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/12/8 12:51
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoPostDeployHandler implements InitializingBean {

    protected final LeoDeployService leoDeployService;

    /**
     * 结束部署通知
     */
    private final EndDeployNotificationChainHandler endDeployNotificationChainHandler;

    /**
     * 最终处理
     */
    private final DeployFinalProcessingChainHandler deployFinalProcessingChainHandler;

    @Async
    public void handleDeploy(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        /*
         * 使用责任链设计模式解耦代码
         */
        endDeployNotificationChainHandler.handleRequest(leoDeployService.getById(leoDeploy.getId()), deployConfig);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /*
         * 装配
         */
        endDeployNotificationChainHandler.setNextHandler(deployFinalProcessingChainHandler);
    }

}