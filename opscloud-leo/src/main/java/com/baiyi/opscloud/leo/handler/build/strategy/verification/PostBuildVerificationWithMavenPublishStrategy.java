package com.baiyi.opscloud.leo.handler.build.strategy.verification;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.constants.BuildTypeConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.handler.build.strategy.verification.base.BasePostBuildVerificationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Maven 发布
 * @Author baiyi
 * @Date 2023/4/20 14:40
 * @Version 1.0
 */
@Slf4j
@Component
public class PostBuildVerificationWithMavenPublishStrategy extends BasePostBuildVerificationStrategy {

    @Override
    public String getBuildType() {
        return BuildTypeConstants.MAVEN_PUBLISH;
    }

    /**
     * 验证 Maven Deploy
     *
     * @param leoBuild
     * @param buildConfig
     */
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        // 不校验
        LeoBuild saveLeoBuild = LeoBuild.builder()
                .id(leoBuild.getId())
                .isActive(true)
                .build();
        leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
    }

}