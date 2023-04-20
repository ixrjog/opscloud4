package com.baiyi.opscloud.leo.handler.deploy.chain.pre;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.handler.deploy.BaseDeployHandler;
import com.baiyi.opscloud.leo.handler.deploy.LeoPostDeployHandler;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.helper.LeoHeartbeatHelper;
import com.baiyi.opscloud.leo.supervisor.DeployingSupervisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/12/6 17:33
 * @Version 1.0
 */
@Slf4j
@Component
public class DeployingSupervisorChainHandler extends BaseDeployHandler {

    @Resource
    private LeoPostDeployHandler leoPostDeployHandler;

    @Resource
    private LeoHeartbeatHelper leoDeployHelper;

    @Autowired
    private ThreadPoolTaskExecutor coreExecutor;


    /**
     * 启动监视器
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        LeoBaseModel.DsInstance dsInstance = deployConfig.getDeploy().getKubernetes().getInstance();
        final String instanceUuid = dsInstance.getUuid();
        KubernetesConfig kubernetesConfig = getKubernetesConfigWithUuid(instanceUuid);
        DeployingSupervisor deployingSupervisor = new DeployingSupervisor(
                this.leoDeployHelper,
                leoDeploy,
                deployService,
                logHelper,
                deployConfig,
                kubernetesConfig.getKubernetes(),
                leoPostDeployHandler
        );
        coreExecutor.execute(deployingSupervisor);
        LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                .id(leoDeploy.getId())
                .deployStatus("启动部署Supervisor阶段: 成功")
                .build();
        save(saveLeoDeploy, "启动部署Supervisor成功: jobId={}, deployNumber={}", leoDeploy.getId(), leoDeploy.getDeployNumber());
    }
}
