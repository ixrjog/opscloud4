package com.baiyi.opscloud.leo.handler.deploy.chain.post;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.constants.DeployStepConstants;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.handler.deploy.BaseDeployChainHandler;
import com.baiyi.opscloud.leo.handler.deploy.base.BaseDeployStrategy;
import com.baiyi.opscloud.leo.handler.deploy.base.DeployStrategyFactory;
import com.baiyi.opscloud.leo.handler.deploy.base.IDeployStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/12/8 12:50
 * @Version 1.0
 */
@Slf4j
@Component
public class EndDeployNotificationChainHandler extends BaseDeployChainHandler implements IDeployStep {

    /**
     * 部署结束通知
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        BaseDeployStrategy deployStrategy = DeployStrategyFactory.getStrategy(getStep(), deployConfig.getDeploy().getDeployType());
        // 基于策略模式实现
        deployStrategy.handleRequest(leoDeploy, deployConfig);
    }

    @Override
    public String getStep() {
        return DeployStepConstants.END_NOTIFICATION.name();
    }

}