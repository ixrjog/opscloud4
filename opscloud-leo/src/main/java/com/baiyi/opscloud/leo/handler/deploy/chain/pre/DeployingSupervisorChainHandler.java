package com.baiyi.opscloud.leo.handler.deploy.chain.pre;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.handler.deploy.BaseDeployChainHandler;
import com.baiyi.opscloud.leo.handler.deploy.LeoPostDeployHandler;
import com.baiyi.opscloud.leo.holder.LeoHeartbeatHolder;
import com.baiyi.opscloud.leo.supervisor.DeployingSupervisor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/12/6 17:33
 * @Version 1.0
 */
@Slf4j
@Component
public class DeployingSupervisorChainHandler extends BaseDeployChainHandler {

    @Resource
    private LeoPostDeployHandler leoPostDeployHandler;

    @Resource
    private LeoHeartbeatHolder leoDeployHelper;

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
                leoLog,
                deployConfig,
                kubernetesConfig.getKubernetes(),
                leoPostDeployHandler
        );
        // JDK21 VirtualThreads
        Thread.ofVirtual().start(deployingSupervisor);
        LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                .id(leoDeploy.getId())
                .deployStatus("启动部署Supervisor阶段: 成功")
                .build();
        save(saveLeoDeploy, "启动部署Supervisor成功: deployId={}, deployNumber={}", leoDeploy.getId(), leoDeploy.getDeployNumber());
    }

}