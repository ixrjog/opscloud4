package com.baiyi.opscloud.leo.handler.build.chain.post;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.constants.BuildStepConstants;
import com.baiyi.opscloud.leo.constants.BuildTypeConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.handler.build.BaseBuildChainHandler;
import com.baiyi.opscloud.leo.handler.build.base.BaseBuildStrategy;
import com.baiyi.opscloud.leo.handler.build.base.BuildStrategyFactory;
import com.baiyi.opscloud.leo.handler.build.base.IBuildStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/4/20 10:49
 * @Version 1.0
 */
@Slf4j
@Component
public class PostBuildVerificationChainHandler extends BaseBuildChainHandler implements IBuildStep {

    /**
     * 执行部署
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        // 获取buildType
        final String buildType = Optional.ofNullable(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getType)
                .orElse(BuildTypeConstants.KUBERNETES_IMAGE);
        BaseBuildStrategy buildStrategy = BuildStrategyFactory.getStrategy(getStep(), buildType);
        // 基于策略模式实现
        buildStrategy.handleRequest(leoBuild, buildConfig);
    }

    @Override
    public String getStep() {
        return BuildStepConstants.POST_BUILD_VERIFICATION.name();
    }

}