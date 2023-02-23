package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.instance.OcInstance;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployVO;
import com.baiyi.opscloud.facade.leo.LeoDeployFacade;
import com.baiyi.opscloud.leo.action.deploy.LeoDeployHandler;
import com.baiyi.opscloud.leo.annotation.LeoDeployInterceptor;
import com.baiyi.opscloud.leo.constants.ExecutionTypeConstants;
import com.baiyi.opscloud.leo.delegate.LeoBuildDeploymentDelegate;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.interceptor.LeoExecuteJobInterceptorHandler;
import com.baiyi.opscloud.leo.packer.LeoDeployResponsePacker;
import com.baiyi.opscloud.leo.supervisor.DeployingSupervisor;
import com.baiyi.opscloud.packer.leo.LeoBuildVersionPacker;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
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

    private final LeoJobService jobService;

    private final EnvService envService;

    private final ApplicationResourceService applicationResourceService;

    private final ApplicationService applicationService;

    private final LeoBuildService buildService;

    private final LeoBuildVersionPacker buildVersionPacker;

    private final LeoDeployService deployService;

    private final LeoDeployHandler deployHandler;

    private final UserService userService;

    private final LeoDeployResponsePacker deployResponsePacker;

    private final LeoExecuteJobInterceptorHandler executeJobInterceptorHandler;

    private final RedisUtil redisUtil;

    private final LeoBuildDeploymentDelegate buildDeploymentDelegate;

    @Override
    @LeoDeployInterceptor(jobIdSpEL = "#doDeploy.jobId", deployTypeSpEL = "#doDeploy.deployType")
    public void doDeploy(LeoDeployParam.DoDeploy doDeploy) {
        // 执行部署任务
        LeoJob leoJob = jobService.getById(doDeploy.getJobId());
        final int deployNumber = generateDeployNumberWithJobId(leoJob.getId());

        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob.getJobConfig());
        // 部署通知
        LeoBaseModel.Notify notify = Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getDeploy)
                .map(LeoJobModel.Deploy::getNotify)
                .orElseThrow(() -> new LeoDeployException("部署通知配置不存在！"));

        LeoBaseModel.Kubernetes kubernetes = LeoBaseModel.Kubernetes.builder()
                .assetId(doDeploy.getAssetId())
                .build();

        LeoDeployModel.Deploy deploy = LeoDeployModel.Deploy.builder()
                .deployType(doDeploy.getDeployType())
                .notify(notify)
                .kubernetes(kubernetes)
                .build();

        LeoDeployModel.DeployConfig deployConfig = LeoDeployModel.DeployConfig.builder()
                .deploy(deploy)
                .build();

        LeoDeploy newLeoDeploy = LeoDeploy.builder()
                .applicationId(leoJob.getApplicationId())
                .jobId(leoJob.getId())
                .jobName(leoJob.getName())
                .buildId(doDeploy.getBuildId() == null ? 0 : doDeploy.getBuildId())
                .deployNumber(deployNumber)
                .deployConfig(deployConfig.dump())
                .executionType(ExecutionTypeConstants.USER)
                .username(SessionUtil.getUsername())
                .isFinish(false)
                .isActive(true)
                .isRollback(false)
                .ocInstance(OcInstance.ocInstance)
                .build();
        deployService.add(newLeoDeploy);
        handleDeploy(newLeoDeploy, deployConfig);
    }

    /**
     * 使用责任链设计模式解耦代码
     *
     * @param leoDeploy
     * @param deployConfig
     */
    private void handleDeploy(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        deployHandler.handleDeploy(leoDeploy, deployConfig);
    }

    /**
     * 生成构建编号
     * 当前最大值 + 1
     *
     * @param jobId
     * @return
     */
    private int generateDeployNumberWithJobId(int jobId) {
        return deployService.getMaxDeployNumberWithJobId(jobId) + 1;
    }

    @Override
    public List<LeoBuildVO.Build> queryLeoDeployVersion(LeoBuildParam.QueryDeployVersion queryBuildVersion) {
        List<LeoBuild> builds = buildService.queryBuildVersion(queryBuildVersion);
        return BeanCopierUtil.copyListProperties(builds, LeoBuildVO.Build.class).stream()
                .peek(e -> buildVersionPacker.wrap(e, queryBuildVersion))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResourceVO.BaseResource> queryLeoBuildDeployment(LeoBuildParam.QueryDeployDeployment queryBuildDeployment) {
        return buildDeploymentDelegate.queryLeoBuildDeployment(queryBuildDeployment.getJobId());
    }

    @Override
    public DataTable<LeoDeployVO.Deploy> queryLeoJobDeployPage(LeoJobParam.JobDeployPageQuery pageQuery) {
        List<LeoJob> leoJobs = jobService.queryJobWithApplicationIdAndEnvType(pageQuery.getApplicationId(), pageQuery.getEnvType());
        if (CollectionUtils.isEmpty(leoJobs)) {
            return DataTable.EMPTY;
        }
        List<Integer> jobIds = leoJobs.stream().map(LeoJob::getId).collect(Collectors.toList());
        pageQuery.setJobIds(jobIds);
        DataTable<LeoDeploy> table = deployService.queryDeployPage(pageQuery);
        List<LeoDeployVO.Deploy> data = BeanCopierUtil.copyListProperties(table.getData(), LeoDeployVO.Deploy.class).stream()
                .peek(deployResponsePacker::wrap)
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void stopDeploy(int deployId) {
        LeoDeploy leoDeploy = deployService.getById(deployId);
        if (leoDeploy == null) {
            throw new LeoDeployException("部署记录不存在: deployId={}", deployId);
        }
        LeoJob leoJob = jobService.getById(leoDeploy.getJobId());
        final String username = SessionUtil.getUsername();
        if (executeJobInterceptorHandler.isAdmin(username)) {
            // 管理员操作，跳过验证
        } else {
            // 权限校验
            executeJobInterceptorHandler.verifyAuthorization(leoJob.getId());
        }
        // 设置信号量
        redisUtil.set(String.format(DeployingSupervisor.STOP_SIGNAL, deployId), username, 100L);
    }

}
