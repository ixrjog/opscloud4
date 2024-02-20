package com.baiyi.opscloud.leo.handler.deploy.chain.post.fork;

import com.baiyi.opscloud.common.event.SimpleEvent;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.constants.EventActionTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.leo.delegate.LeoBuildDeploymentDelegate;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.service.leo.LeoJobService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/2/20 10:58
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class ForkToStable {

    private final LeoJobService leoJobService;

    private final LeoBuildDeploymentDelegate leoBuildDeploymentDelegate;

    private final ApplicationEventPublisher applicationEventPublisher;

    public void doFork(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        // 异常处理
        try {
            handle(leoDeploy, deployConfig);
        } catch (LeoDeployException e) {
            log.warn("Leo deploy fork failed: {}", e.getMessage());
        }
    }

    public void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        // Fork baseline
        LeoJob leoJob = leoJobService.getById(leoDeploy.getJobId());
        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob);

        Optional<LeoJobModel.Fork> optionalFork = Optional.of(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getDeploy)
                .map(LeoJobModel.Deploy::getFork);
        if (optionalFork.isEmpty()) {
            return;
        }
        LeoJobModel.Fork fork = optionalFork.get();

        boolean enabled = Optional.of(fork)
                .map(LeoJobModel.Fork::getEnabled)
                .orElse(false);
        if (!enabled) {
            // 未启用 Fork baseline
            return;
        }

        LeoJob stableJob = getStableJob(fork, leoJob);
        List<ApplicationResourceVO.BaseResource> stableDeployments = getStableDeployments(fork, stableJob);

        if (!CollectionUtils.isEmpty(stableDeployments)) {
            stableDeployments.stream().map(stableDeployment -> LeoDeployParam.DoForkDeploy.builder()
                            .username(leoDeploy.getUsername())
                            .buildId(leoDeploy.getBuildId())
                            .assetId(stableDeployment.getBusinessId())
                            .deployType(DeployTypeConstants.ROLLING.name())
                            .build()).map(doDeploy -> SimpleEvent.<LeoDeployParam.DoForkDeploy>builder()
                            .eventType(BusinessTypeEnum.LEO_FORK_DEPLOY.name())
                            .action(EventActionTypeEnum.CREATE.name())
                            .body(doDeploy)
                            .build())
                    .forEach(applicationEventPublisher::publishEvent);
        }
    }

    private List<ApplicationResourceVO.BaseResource> getStableDeployments(LeoJobModel.Fork fork, LeoJob stableJob) {
        List<String> deploymentNames = Optional.of(fork)
                .map(LeoJobModel.Fork::getStable)
                .map(LeoJobModel.Stable::getDeployments)
                .orElse(Collections.emptyList());
        List<ApplicationResourceVO.BaseResource> result = leoBuildDeploymentDelegate.queryLeoBuildDeployment(stableJob.getId());

        if (!CollectionUtils.isEmpty(deploymentNames)) {
            return result.stream().filter(e -> deploymentNames.stream().anyMatch(deploymentName -> deploymentName.equalsIgnoreCase(e.getName()))).toList();
        }
        if (CollectionUtils.isEmpty(result)) {
            throw new LeoDeployException("There is no associated deployment for the job.");
        } else {
            return result.stream().filter(e -> e.getName().endsWith("-daily")).toList();
        }
    }

    private LeoJob getStableJob(LeoJobModel.Fork fork, LeoJob leoJob) {
        String jobName = Optional.of(fork)
                .map(LeoJobModel.Fork::getStable)
                .map(LeoJobModel.Stable::getJob)
                .orElse(null);
        // 2 = daily
        List<LeoJob> leoJobs = leoJobService.queryJob(leoJob.getApplicationId(), 2);
        if (StringUtils.hasText(jobName)) {
            return leoJobs.stream().filter(e -> jobName.equalsIgnoreCase(e.getName())).findFirst().orElseThrow(() -> new LeoDeployException("Fork configuration error, {} does not exist.", jobName));
        }
        if (leoJobs.size() == 1) {
            return leoJobs.getFirst();
        } else {
            throw new LeoDeployException("Fork configuration error, missing job configuration and multiple job.");
        }
    }

}
