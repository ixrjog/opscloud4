package com.baiyi.opscloud.leo.action.deploy.strategy.inspection;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.action.deploy.strategy.inspection.base.PreInspectionStrategy;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 灰度预检查策略
 *
 * @Author baiyi
 * @Date 2022/12/13 15:16
 * @Version 1.0
 */
@Slf4j
@Component
public class PreInspectionWithGrayStrategy extends PreInspectionStrategy {

    /**
     * 预检查
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {

    }

    @Override
    public String getDeployType() {
        return DeployTypeConstants.GRAY.name();
    }

}
