package com.baiyi.opscloud.leo.handler.deploy.strategy.boot;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.handler.deploy.strategy.boot.base.BootDeployStrategy;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/15 19:59
 * @Version 1.0
 */
@Slf4j
@Component
public class BootDeployWithRollingStrategy extends BootDeployStrategy {

    @Resource
    private LeoBuildService leoBuildService;

    @Override
    protected void preHandle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        if (IdUtil.isEmpty(leoDeploy.getBuildId())) {
            throw new LeoDeployException("发布版本不存在");
        }
        LeoBuild leoBuild = leoBuildService.getById(leoDeploy.getBuildId());
        if (!leoBuild.getIsActive()) {
            throw new LeoDeployException("构建版本无效: buildId={}, isActive=false", leoDeploy.getBuildId());
        }
        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.load(leoBuild.getBuildConfig());
        // 从构建中获取字典
        Map<String, String> dict = Optional.ofNullable(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getDict)
                .orElseThrow(() -> new LeoDeployException("构建字典不存在！"));

        deployConfig.getDeploy().setDict(dict);
        leoDeploy.setVersionName(leoBuild.getVersionName());
        leoDeploy.setVersionDesc(leoBuild.getVersionDesc());
    }

    @Override
    public String getDeployType() {
        return DeployTypeConstants.ROLLING.name();
    }

}