package com.baiyi.opscloud.leo.handler.build.strategy.verification;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildImage;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.constants.BuildTypeConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.handler.build.strategy.verification.base.BasePostBuildVerificationStrategy;
import com.baiyi.opscloud.leo.handler.build.strategy.verification.validator.base.BaseCrValidator;
import com.baiyi.opscloud.leo.handler.build.strategy.verification.validator.factory.CrValidatorFactory;
import com.baiyi.opscloud.service.leo.LeoBuildImageService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/4/20 14:45
 * @Version 1.0
 */
@Slf4j
@Component
public class PostBuildVerificationWithKubernetesImageStrategy extends BasePostBuildVerificationStrategy {

    @Resource
    private LeoJobService leoJobService;

    @Resource
    private LeoBuildImageService leoBuildImageService;

    @Override
    public String getBuildType() {
        return BuildTypeConstants.KUBERNETES_IMAGE;
    }

    /**
     * 验证 Kubernetes Image
     *
     * @param leoBuild
     * @param buildConfig
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        LeoJob leoJob = leoJobService.getById(leoBuild.getJobId());
        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob.getJobConfig());
        final LeoJobModel.CR cr = Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getCr)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: job->cr"));

        final String crType = Optional.of(cr)
                .map(LeoJobModel.CR::getType)
                .orElseThrow(() -> new LeoBuildException("Configuration does not exist: job->cr->type"));

        BaseCrValidator crValidator = CrValidatorFactory.getValidatorByCrType(crType);
        if (crValidator == null) {
            throw new LeoBuildException("Incorrect configuration: job->cr->type={}", crType);
        }
        try {
            // 校验
            crValidator.verifyImage(leoJob, leoBuild, cr, buildConfig);
            // 记录构建镜像
            Map<String, String> dict = buildConfig.getBuild().getDict();
            LeoBuildImage leoBuildImage = LeoBuildImage.builder()
                    .buildId(leoBuild.getId())
                    .jobId(leoJob.getId())
                    .image(dict.get(BuildDictConstants.IMAGE.getKey()))
                    .versionName(leoBuild.getVersionName())
                    .versionDesc(leoBuild.getVersionDesc())
                    .isActive(true)
                    .build();
            leoBuildImageService.add(leoBuildImage);
            // 设置构建任务有效
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .isActive(true)
                    .build();
            leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
        } catch (LeoBuildException e) {
            leoLog.error(leoBuild, e.getMessage());
        }
    }

}