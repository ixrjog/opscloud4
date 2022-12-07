package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.facade.leo.LeoDeployFacade;
import com.baiyi.opscloud.leo.action.deploy.LeoDeployHandler;
import com.baiyi.opscloud.leo.constants.ExecutionTypeConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.packer.leo.LeoBuildVersionPacker;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.sys.EnvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/12/5 14:31
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeoDeployFacadeImpl implements LeoDeployFacade {

    private final LeoJobService leoJobService;

    private final EnvService envService;

    private final ApplicationResourceService applicationResourceService;

    private final LeoBuildService leoBuildService;

    private final LeoBuildVersionPacker leoBuildVersionPacker;

    private final LeoDeployService leoDeployService;

    private final LeoDeployHandler leoDeployHandler;

    @Override
    public void doDeploy(LeoDeployParam.DoDeploy doDeploy) {
        // 执行部署任务
        LeoJob leoJob = leoJobService.getById(doDeploy.getJobId());
        LeoBuild leoBuild = leoBuildService.getById(doDeploy.getBuildId());
        final int deployNumber = generateDeployNumberWithJobId(leoJob.getId());

        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob.getJobConfig());
        // 部署通知
        LeoBaseModel.Notify notify = Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getDeploy)
                .map(LeoJobModel.Deploy::getNotify)
                .orElseThrow(() -> new LeoDeployException("部署通知配置不存在！"));
        // 字典
        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.load(leoBuild.getBuildConfig());
        Map<String, String> dict = Optional.ofNullable(buildConfig)
                .map(LeoBuildModel.BuildConfig::getBuild)
                .map(LeoBuildModel.Build::getDict)
                .orElseThrow(() -> new LeoDeployException("构建字典不存在！"));

        LeoBaseModel.Kubernetes kubernetes =  LeoBaseModel.Kubernetes.builder()
                .assetId(doDeploy.getAssetId())
                .build();

        LeoDeployModel.Deploy deploy = LeoDeployModel.Deploy.builder()
                .deployType(doDeploy.getDeployType())
                .notify(notify)
                .dict(dict)
                .kubernetes(kubernetes)
                .build();

        LeoDeployModel.DeployConfig deployConfig = LeoDeployModel.DeployConfig.builder()
                .deploy(deploy)
                .build();

        LeoDeploy leoDeploy = LeoDeploy.builder()
                .applicationId(leoJob.getApplicationId())
                .jobId(leoJob.getId())
                .jobName(leoJob.getName())
                .buildId(doDeploy.getBuildId())
                .deployNumber(deployNumber)
                .deployConfig(deployConfig.dump())
                .versionName(leoBuild.getVersionName())
                .versionDesc(leoBuild.getVersionDesc())
                .executionType(ExecutionTypeConstants.USER)
                .username(SessionUtil.getUsername())
                .isFinish(false)
                .isActive(true)
                .isRollback(false)
                .build();

        leoDeployService.add(leoDeploy);
        handleDeploy(leoDeploy, deployConfig);
    }

    /**
     * 使用责任链设计模式解耦代码
     * @param leoDeploy
     * @param deployConfig
     */
    private void handleDeploy(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        leoDeployHandler.handleDeploy(leoDeploy, deployConfig);
    }

    /**
     * 生成构建编号
     * 当前最大值 + 1
     *
     * @param jobId
     * @return
     */
    private int generateDeployNumberWithJobId(int jobId) {
        return leoDeployService.getMaxDeployNumberWithJobId(jobId) + 1;
    }

    @Override
    public List<LeoBuildVO.Build> queryLeoDeployVersion(LeoBuildParam.QueryDeployVersion queryBuildVersion) {
        List<LeoBuild> builds = leoBuildService.queryBuildVersion(queryBuildVersion);
        return BeanCopierUtil.copyListProperties(builds, LeoBuildVO.Build.class).stream()
                .peek(e -> leoBuildVersionPacker.wrap(e, queryBuildVersion))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResourceVO.BaseResource> queryLeoBuildDeployment(LeoBuildParam.QueryDeployDeployment queryBuildDeployment) {
        LeoJob leoJob = leoJobService.getById(queryBuildDeployment.getJobId());
        Env env = envService.getByEnvType(leoJob.getEnvType());
        List<ApplicationResource> resources = applicationResourceService.queryByApplication(leoJob.getApplicationId(),
                DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name(),
                BusinessTypeEnum.ASSET.getType());
        if (CollectionUtils.isEmpty(resources))
            return Collections.emptyList();
        final String envName = env.getEnvName();
        List<ApplicationResource> result = resources.stream().filter(e -> {
            if (e.getName().startsWith(envName + ":")) return true;
            // TODO 环境标准化后以下代码可以删除
            if (env.getEnvName().equals("dev")) {
                return e.getName().startsWith("ci:");
            }
            if (env.getEnvName().equals("daily")) {
                return e.getName().startsWith("test:");
            }
            if (env.getEnvName().equals("prod")) {
                if (e.getName().startsWith("gray:")) return true;
                return e.getName().startsWith("canary:");
            }
            return false;
        }).collect(Collectors.toList());
        return BeanCopierUtil.copyListProperties(result, ApplicationResourceVO.BaseResource.class);
    }

}
