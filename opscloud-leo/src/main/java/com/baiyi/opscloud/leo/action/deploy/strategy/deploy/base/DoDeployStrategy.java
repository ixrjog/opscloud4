package com.baiyi.opscloud.leo.action.deploy.strategy.deploy.base;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.action.deploy.base.BaseDeployStrategy;
import com.baiyi.opscloud.leo.constants.DeployDictConstants;
import com.baiyi.opscloud.leo.constants.DeployStepConstants;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import io.fabric8.kubernetes.api.model.apps.Deployment;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/14 10:45
 * @Version 1.0
 */
public abstract class DoDeployStrategy extends BaseDeployStrategy {

    @Resource
    protected DsInstanceAssetService assetService;

    @Resource
    protected DsInstanceService instanceService;

    /**
     * 执行部署
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        LeoBaseModel.Kubernetes kubernetes = Optional.ofNullable(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .orElseThrow(() -> new LeoDeployException("Kubernetes配置不存在！"));

        final String instanceUuid = kubernetes.getInstance().getUuid();
        final String namespace = kubernetes.getDeployment().getNamespace();
        final String deploymentName = kubernetes.getDeployment().getName();
        KubernetesConfig kubernetesConfig = getKubernetesConfigWithUuid(instanceUuid);
        Deployment deployment = KubernetesDeploymentDriver.getDeployment(kubernetesConfig.getKubernetes(),
                namespace,
                deploymentName);
        if (deployment == null) {
            throw new LeoDeployException("Deployment不存在！");
        }

        Map<String, String> dict = generateDict(leoDeploy, deployConfig);
        deployConfig.getDeploy().setDict(dict);

        doDeploy(leoDeploy, deployConfig, kubernetesConfig, deployment);
    }

    private Map<String, String> generateDict(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        Map<String, String> dict = Optional.ofNullable(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getDict)
                .orElseThrow(() -> new LeoDeployException("部署字典不存在！"));

        final String deployType = deployConfig.getDeploy().getDeployType();

        dict.put(DeployDictConstants.DEPLOY_NUMBER.getKey(), String.valueOf(leoDeploy.getDeployNumber()));
        dict.put(DeployDictConstants.DEPLOY_TYPE.getKey(), deployType);
        dict.put(DeployDictConstants.DEPLOY_TYPE_DESC.getKey(), DeployTypeConstants.getDesc(deployType));
        return dict;
    }

    abstract protected void doDeploy(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig, KubernetesConfig kubernetesConfig, Deployment deployment);


    @Override
    public String getStep() {
        return DeployStepConstants.DO_DEPLOY.name();
    }

}
