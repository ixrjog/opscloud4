package com.baiyi.opscloud.leo.handler.deploy.strategy.inspection;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildImage;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.handler.deploy.strategy.inspection.base.BasePreInspectionStrategy;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 滚动预检查策略
 * @Author baiyi
 * @Date 2022/12/12 20:43
 * @Version 1.0
 */
@Slf4j
@Component
public class PreInspectionWithRollingStrategy extends BasePreInspectionStrategy {

    /**
     * 预检查
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        preHandle(leoDeploy, deployConfig);
        LeoDeployModel.Deploy deploy = deployConfig.getDeploy();

        final String image = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .map(LeoBaseModel.Kubernetes::getDeployment)
                .map(LeoBaseModel.Deployment::getContainer)
                .map(LeoBaseModel.Container::getImage)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->kubernetes->deployment->container->image"));

        Map<String, String> dict = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getDict)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->dict"));

        List<LeoBuildImage> leoBuildImages = leoBuildImageService.queryImageWithJobIdAndImage(leoDeploy.getJobId(), image);
        /*
         * 以前版本
         */
        LeoDeployModel.DeployVersion previousVersion;
        if (CollectionUtils.isEmpty(leoBuildImages)) {
            previousVersion = LeoDeployModel.DeployVersion.UNKNOWN;
            previousVersion.setImage(image);
        } else {
            LeoBuildImage leoBuildImage = leoBuildImages.getFirst();
            previousVersion = LeoDeployModel.DeployVersion.builder()
                    .buildId(leoBuildImage.getBuildId())
                    .versionName(leoBuildImage.getVersionName())
                    .versionDesc(leoBuildImage.getVersionDesc())
                    .image(leoBuildImage.getImage())
                    .comment("Previous Version")
                    .build();
        }
        /*
         * 发布版本
         */
        LeoBuild leoBuild = leoBuildService.getById(leoDeploy.getBuildId());
        LeoDeployModel.DeployVersion releaseVersion = LeoDeployModel.DeployVersion.builder()
                .buildId(leoDeploy.getBuildId())
                .versionName(leoBuild.getVersionName())
                .versionDesc(leoBuild.getVersionDesc())
                .image(dict.get(BuildDictConstants.IMAGE.getKey()))
                .comment("Release Version")
                .build();

        if (releaseVersion.getImage().equals(previousVersion.getImage())) {
            throw new LeoDeployException("预检查失败发布镜像未变更: image={}", releaseVersion.getImage());
        }

        deploy.setDeployVersion1(previousVersion);
        deploy.setDeployVersion2(releaseVersion);

        LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                .id(leoDeploy.getId())
                .deployConfig(deployConfig.dump())
                .deployStatus("校验Kubernetes阶段: 成功")
                .build();
        save(saveLeoDeploy, "校验Kubernetes成功");
    }

    @Override
    public String getDeployType() {
        return DeployTypeConstants.ROLLING.name();
    }

}