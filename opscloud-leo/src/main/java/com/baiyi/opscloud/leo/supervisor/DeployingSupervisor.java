package com.baiyi.opscloud.leo.supervisor;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.util.NewTimeUtil;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.constants.HeartbeatTypeConstants;
import com.baiyi.opscloud.leo.handler.deploy.BaseDeployChainHandler;
import com.baiyi.opscloud.leo.handler.deploy.LeoPostDeployHandler;
import com.baiyi.opscloud.leo.domain.model.StopDeployFlag;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.log.LeoDeployingLog;
import com.baiyi.opscloud.leo.holder.LeoHeartbeatHolder;
import com.baiyi.opscloud.leo.supervisor.base.ISupervisor;
import com.baiyi.opscloud.leo.supervisor.strategy.base.SupervisingStrategy;
import com.baiyi.opscloud.leo.supervisor.strategy.base.SupervisingStrategyFactory;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Optional;

import static com.baiyi.opscloud.common.util.NewTimeUtil.MINUTE_TIME;

/**
 * 部署监督
 *
 * @Author baiyi
 * @Date 2022/12/6 14:52
 * @Version 1.0
 */
@Slf4j
public class DeployingSupervisor implements ISupervisor {

    private static final int SLEEP_SECONDS = 4;

    private final LeoHeartbeatHolder heartbeatHolder;

    private final LeoDeployModel.DeployConfig deployConfig;

    private final LeoDeploy leoDeploy;

    private final KubernetesConfig.Kubernetes kubernetes;

    private final LeoDeployingLog leoLog;

    private final LeoPostDeployHandler postDeployHandler;

    private final LeoDeployService deployService;

    private static final long TIMEOUT = 30 * MINUTE_TIME;

    public DeployingSupervisor(LeoHeartbeatHolder heartbeatHolder,
                               LeoDeploy leoDeploy,
                               LeoDeployService deployService,
                               LeoDeployingLog leoLog,
                               LeoDeployModel.DeployConfig deployConfig,
                               KubernetesConfig.Kubernetes kubernetes,
                               LeoPostDeployHandler postDeployHandler) {
        this.heartbeatHolder = heartbeatHolder;
        this.leoDeploy = leoDeploy;
        this.deployService = deployService;
        this.leoLog = leoLog;
        this.deployConfig = deployConfig;
        this.kubernetes = kubernetes;
        this.postDeployHandler = postDeployHandler;
    }

    @Override
    public boolean tryStop() {
        StopDeployFlag deployStop = heartbeatHolder.getStopDeployFlag(leoDeploy.getId());
        if (deployStop.getIsStop()) {
            LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .deployResult(BaseDeployChainHandler.RESULT_ERROR)
                    .endTime(new Date())
                    .isFinish(true)
                    .isActive(false)
                    .deployStatus(StringFormatter.format("{} 手动停止任务", deployStop.getUsername()))
                    .build();
            deployService.updateByPrimaryKeySelective(saveLeoDeploy);
            leoLog.error(leoDeploy, StringFormatter.format("{} 手动停止任务", deployStop.getUsername()));
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        LeoDeployModel.Deploy deploy = Optional.ofNullable(this.deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy"));

        LeoBaseModel.Deployment deployment = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .map(LeoBaseModel.Kubernetes::getDeployment)
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->kubernetes->deployment"));

        SupervisingStrategy deployingStrategy = SupervisingStrategyFactory.getStrategyByDeployType(deploy.getDeployType());
        if (deployingStrategy == null) {
            leoLog.error(this.leoDeploy, "未找到对应的部署策略: deployType={}", deploy.getDeployType());
            throw new LeoDeployException("未找到对应的部署策略: deployType={}", deploy.getDeployType());
        }
        // 循环
        while (true) {
            setHeartbeat();
            if (tryStop()) {
                return;
            }
            try {
                deployingStrategy.handle(leoDeploy, deployConfig, kubernetes, deploy, deployment);
                if (heartbeatHolder.isFinish(leoDeploy.getId())) {
                    // 结束
                    postHandle();
                    break;
                }
            } catch (Exception e) {
                leoLog.warn(this.leoDeploy, e.getMessage());
            }
            // 延迟执行
            NewTimeUtil.sleep(SLEEP_SECONDS);
        }
    }

    public boolean isTimeout() {
        return com.baiyi.opscloud.common.util.NewTimeUtil.isTimeout(leoDeploy.getStartTime().getTime(), TIMEOUT);
    }

    private void setHeartbeat() {
        heartbeatHolder.setHeartbeat(HeartbeatTypeConstants.DEPLOY, leoDeploy.getId());
    }

    private void postHandle() {
        postDeployHandler.handleDeploy(leoDeploy, deployConfig);
    }

}