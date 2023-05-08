package com.baiyi.opscloud.leo.handler.build.chain.post;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.leo.delegate.LeoBuildDeploymentDelegate;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.handler.build.BaseBuildChainHandler;
import com.baiyi.opscloud.leo.handler.build.chain.post.event.AutoDeployEventPublisher;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 构建后自动部署
 *
 * @Author baiyi
 * @Date 2023/5/8 13:34
 * @Version 1.0
 */
@Slf4j
@Component
public class AutoDeployChainHandler extends BaseBuildChainHandler {

    @Resource
    private AutoDeployEventPublisher autoDeployEventPublisher;

    @Resource
    private LeoBuildDeploymentDelegate leoBuildDeploymentDelegate;

    /**
     * 记录流水线
     *
     * @param leoBuild
     * @param buildConfig
     */
    @Override

    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        if (!leoBuild.getIsActive()) {
            log.debug("无效的构建，不执行自动部署");
            return;
        }

        LeoBaseModel.AutoDeploy autoDeploy = Optional.ofNullable(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getAutoDeploy)
                .orElse(LeoBaseModel.AutoDeploy.EMPTY);

        if (autoDeploy.isEmpty()) {
            return;
        }

        if (!checkAsset(leoBuild, autoDeploy.getAssetId())) {
            logHelper.info(leoBuild, "构建后执行自动部署错误: assetId校验失败");
            return;
        }

        LeoDeployParam.DoAutoDeploy doAutoDeploy = LeoDeployParam.DoAutoDeploy.builder()
                .assetId(autoDeploy.getAssetId())
                .jobId(leoBuild.getJobId())
                .buildId(leoBuild.getId())
                .deployType(autoDeploy.getDeployType())
                .username(leoBuild.getUsername())
                .build();
        logHelper.info(leoBuild, "构建后执行自动部署");
        // 发布事件
        autoDeployEventPublisher.publish(doAutoDeploy);
    }

    private boolean checkAsset(LeoBuild leoBuild, Integer assetId) {
        List<ApplicationResourceVO.BaseResource> resources = leoBuildDeploymentDelegate.queryLeoBuildDeployment(leoBuild.getJobId());

        return resources.stream().anyMatch(r -> assetId.equals(r.getBusinessId()));
    }

}