package com.baiyi.opscloud.leo.handler.deploy.strategy.notification;

import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.handler.deploy.strategy.notification.base.StartNotificationStrategy;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/12/15 16:18
 * @Version 1.0
 */
@Component
public class StartNotificationWithRedeployStrategy extends StartNotificationStrategy {

    private static final String LEO_REDEPLOY_START = "LEO_REDEPLOY_START";

    @Override
    protected Map<String, Object> buildContentMap(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        return Maps.newHashMap();
    }

    @Override
    public String getDeployType() {
        return DeployTypeConstants.REDEPLOY.name();
    }

    @Override
    protected String getMessageKey() {
        return LEO_REDEPLOY_START;
    }

}