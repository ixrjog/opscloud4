package com.baiyi.opscloud.leo.task;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.instance.OcInstance;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.action.deploy.LeoPostDeployHandler;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.helper.DeployingLogHelper;
import com.baiyi.opscloud.leo.helper.LeoHeartbeatHelper;
import com.baiyi.opscloud.leo.supervisor.DeployingSupervisor;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/26 13:48
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoDeployCompensationTask {

    private final LeoDeployService leoDeployService;

    private final LeoHeartbeatHelper heartbeatHelper;

    private final DeployingLogHelper logHelper;

    private final DsConfigHelper dsConfigHelper;

    private final LeoPostDeployHandler leoPostDeployHandler;

    public void handleTask() {
        List<LeoDeploy> leoDeploys = leoDeployService.queryDeployRunningWithOcInstance(OcInstance.ocInstance);
        if (CollectionUtils.isEmpty(leoDeploys)) {
            return;
        }
        leoDeploys.forEach(leoDeploy -> {
            if (!heartbeatHelper.isLive(LeoHeartbeatHelper.HeartbeatTypes.DEPLOY, leoDeploy.getId())) {
                LeoDeployModel.DeployConfig deployConfig = LeoDeployModel.load(leoDeploy);
                LeoBaseModel.DsInstance dsInstance = deployConfig.getDeploy().getKubernetes().getInstance();
                final String instanceUuid = dsInstance.getUuid();
                KubernetesConfig kubernetesConfig = getKubernetesConfigWithUuid(instanceUuid);
                DeployingSupervisor deployingSupervisor = new DeployingSupervisor(
                        this.heartbeatHelper,
                        leoDeploy,
                        logHelper,
                        deployConfig,
                        kubernetesConfig.getKubernetes(),
                        leoPostDeployHandler
                );
                log.info("执行补偿任务: deployId={}", leoDeploy.getId());
                Thread thread = new Thread(deployingSupervisor);
                thread.start();
            }
        });
    }

    private KubernetesConfig getKubernetesConfigWithUuid(String uuid) {
        DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(uuid);
        return dsConfigHelper.build(dsConfig, KubernetesConfig.class);
    }

}
