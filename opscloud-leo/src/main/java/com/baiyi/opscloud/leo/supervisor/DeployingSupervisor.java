package com.baiyi.opscloud.leo.supervisor;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.action.deploy.BaseDeployHandler;
import com.baiyi.opscloud.leo.action.deploy.LeoPostDeployHandler;
import com.baiyi.opscloud.leo.domain.model.DeployStop;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.helper.DeployingLogHelper;
import com.baiyi.opscloud.leo.helper.LeoHeartbeatHelper;
import com.baiyi.opscloud.leo.supervisor.base.ISupervisor;
import com.baiyi.opscloud.leo.supervisor.strategy.base.SupervisingStrategy;
import com.baiyi.opscloud.leo.supervisor.strategy.base.SupervisingStrategyFactroy;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 部署监督
 *
 * @Author baiyi
 * @Date 2022/12/6 14:52
 * @Version 1.0
 */
@Slf4j
public class DeployingSupervisor implements ISupervisor {

    private static final int SLEEP_SECONDS = 10;

    private final LeoHeartbeatHelper heartbeatHelper;

    private final LeoDeployModel.DeployConfig deployConfig;

    private final LeoDeploy leoDeploy;

    private final KubernetesConfig.Kubernetes kubernetes;

    private final DeployingLogHelper logHelper;

    private final LeoPostDeployHandler postDeployHandler;

    private final LeoDeployService deployService;

    public static final String STOP_SIGNAL = "leo#deploy#stop#id=%s";

    public DeployingSupervisor(LeoHeartbeatHelper heartbeatHelper,
                               LeoDeploy leoDeploy,
                               LeoDeployService deployService,
                               DeployingLogHelper logHelper,
                               LeoDeployModel.DeployConfig deployConfig,
                               KubernetesConfig.Kubernetes kubernetes,
                               LeoPostDeployHandler postDeployHandler) {
        this.heartbeatHelper = heartbeatHelper;
        this.leoDeploy = leoDeploy;
        this.deployService = deployService;
        this.logHelper = logHelper;
        this.deployConfig = deployConfig;
        this.kubernetes = kubernetes;
        this.postDeployHandler = postDeployHandler;
    }

    @Override
    public void run() {
        LeoDeployModel.Deploy deploy = Optional.ofNullable(this.deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .orElseThrow(() -> new LeoDeployException("部署配置不存在！"));

        LeoBaseModel.Deployment deployment = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .map(LeoBaseModel.Kubernetes::getDeployment)
                .orElseThrow(() -> new LeoDeployException("Kubernetes配置不存在！"));
        final String containerName = deployment.getContainer().getName();
        SupervisingStrategy deployingStrategy = SupervisingStrategyFactroy.getStrategyByDeployType(deploy.getDeployType());
        if (deployingStrategy == null) {
            logHelper.error(this.leoDeploy, "未找到对应的部署策略: deployType={}", deploy.getDeployType());
            throw new LeoDeployException("未找到对应的部署策略: deployType={}", deploy.getDeployType());
        }
        // 循环
        while (true) {
            setHeartbeat();
            // tryStop
            DeployStop deployStop = heartbeatHelper.tryDeployStop(leoDeploy.getId());
            if (deployStop.getIsStop()) {
                logHelper.warn(this.leoDeploy, "用户手动停止: username={}", deployStop.getUsername());
                LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                        .id(leoDeploy.getId())
                        .deployResult(BaseDeployHandler.RESULT_ERROR)
                        .endTime(new Date())
                        .isFinish(true)
                        .isActive(false)
                        .deployStatus(String.format("用户手动停止: username=%s", deployStop.getUsername()))
                        .build();
                deployService.updateByPrimaryKeySelective(saveLeoDeploy);
                break;
            }
            try {
                deployingStrategy.handle(leoDeploy, deployConfig, kubernetes, deploy, deployment);
                if (heartbeatHelper.isFinish(leoDeploy.getId())) {
                    // 结束
                    postHandle();
                    break;
                }
            } catch (Exception e) {
                logHelper.warn(this.leoDeploy, e.getMessage());
                log.warn(e.getMessage());
            }
            // 延迟执行
            try {
                TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void setHeartbeat() {
        heartbeatHelper.setHeartbeat(LeoHeartbeatHelper.HeartbeatTypes.DEPLOY, leoDeploy.getId());
    }

    private void postHandle() {
        postDeployHandler.handleDeploy(leoDeploy, deployConfig);
    }

}
