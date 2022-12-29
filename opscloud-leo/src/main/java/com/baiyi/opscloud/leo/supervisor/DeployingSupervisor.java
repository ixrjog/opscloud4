package com.baiyi.opscloud.leo.supervisor;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.action.deploy.LeoPostDeployHandler;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.helper.DeployingLogHelper;
import com.baiyi.opscloud.leo.helper.LeoDeployHelper;
import com.baiyi.opscloud.leo.supervisor.base.ISupervisor;
import com.baiyi.opscloud.leo.supervisor.strategy.base.SupervisingStrategy;
import com.baiyi.opscloud.leo.supervisor.strategy.base.SupervisingStrategyFactroy;
import lombok.extern.slf4j.Slf4j;

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

    //private final LeoDeployService leoDeployService;

    private final LeoDeployHelper leoDeployHelper;

    private final LeoDeployModel.DeployConfig deployConfig;

    private final LeoDeploy leoDeploy;

    private final KubernetesConfig.Kubernetes kubernetes;

    private final DeployingLogHelper logHelper;

    private final LeoPostDeployHandler leoPostDeployHandler;

    public DeployingSupervisor(LeoDeployHelper leoDeployHelper,
                               LeoDeploy leoDeploy,
                               DeployingLogHelper logHelper,
                               LeoDeployModel.DeployConfig deployConfig,
                               KubernetesConfig.Kubernetes kubernetes,
                               LeoPostDeployHandler leoPostDeployHandler) {
        this.leoDeployHelper = leoDeployHelper;
        this.leoDeploy = leoDeploy;
        this.logHelper = logHelper;
        this.deployConfig = deployConfig;
        this.kubernetes = kubernetes;
        this.leoPostDeployHandler = leoPostDeployHandler;
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
            throw new LeoDeployException("未找到对应的部署策略: deployType={}", deploy.getDeployType());
        }
        // 循环
        while (true) {
            setHeartbeat();
            try {
                deployingStrategy.handle(leoDeploy, deployConfig, kubernetes, deploy, deployment);
                if (leoDeployHelper.isFinish(leoDeploy.getId())) {
                    // 结束
                    postHandle();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.warn(e.getMessage());
            }
            // 延迟执行
            try {
                TimeUnit.SECONDS.sleep(SLEEP_SECONDS);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void setHeartbeat(){
        leoDeployHelper.setHeartbeat(leoDeploy.getId());
    }

    private void postHandle() {
        leoPostDeployHandler.handleDeploy(leoDeploy, deployConfig);
    }

}
