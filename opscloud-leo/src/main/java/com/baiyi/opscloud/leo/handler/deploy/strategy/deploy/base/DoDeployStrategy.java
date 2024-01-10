package com.baiyi.opscloud.leo.handler.deploy.strategy.deploy.base;

import com.baiyi.opscloud.common.base.Global;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.constants.BuildDictConstants;
import com.baiyi.opscloud.leo.handler.deploy.base.BaseDeployStrategy;
import com.baiyi.opscloud.leo.constants.DeployDictConstants;
import com.baiyi.opscloud.leo.constants.DeployStepConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.apps.Deployment;

import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/12/14 10:45
 * @Version 1.0
 */
public abstract class DoDeployStrategy extends BaseDeployStrategy {

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
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->kubernetes"));

        final String instanceUuid = kubernetes.getInstance().getUuid();
        final String namespace = kubernetes.getDeployment().getNamespace();
        final String deploymentName = kubernetes.getDeployment().getName();
        KubernetesConfig kubernetesConfig = getKubernetesConfigWithUuid(instanceUuid);
        Deployment deployment = KubernetesDeploymentDriver.get(kubernetesConfig.getKubernetes(),
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
                .orElseThrow(() -> new LeoDeployException("Configuration does not exist: deploy->dict"));

        final String deployType = deployConfig.getDeploy().getDeployType();

        dict.put(DeployDictConstants.DEPLOY_NUMBER.getKey(), String.valueOf(leoDeploy.getDeployNumber()));
        dict.put(DeployDictConstants.DEPLOY_TYPE.getKey(), deployType);
        dict.put(DeployDictConstants.DEPLOY_TYPE_DESC.getKey(), DeployTypeConstants.getDesc(deployType));
        return dict;
    }

    abstract protected void doDeploy(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig, KubernetesConfig kubernetesConfig, Deployment deployment);

    protected int calcReplicas(LeoDeployModel.DeployConfig deployConfig) {
        Map<String, String> dict = Optional.of(deployConfig)
                .map(LeoDeployModel.DeployConfig::getDeploy)
                .map(LeoDeployModel.Deploy::getDict)
                .orElse(Maps.newHashMap());
        if (dict.containsKey(BuildDictConstants.ENV.getKey()) && Global.ENV_PROD.equalsIgnoreCase(dict.get(BuildDictConstants.ENV.getKey()))) {
            String deploymentName = Optional.of(deployConfig)
                    .map(LeoDeployModel.DeployConfig::getDeploy)
                    .map(LeoDeployModel.Deploy::getKubernetes)
                    .map(LeoBaseModel.Kubernetes::getDeployment)
                    .map(LeoBaseModel.Deployment::getName)
                    .orElse("");
            // 生产环境 3 副本 / Canary 1 副本
            return deploymentName.endsWith("-canary") ? 1 : 3;
        } else {
            // 非生产环境 3 副本
            return 1;
        }
    }

    @Override
    public String getStep() {
        return DeployStepConstants.DO_DEPLOY.name();
    }

}