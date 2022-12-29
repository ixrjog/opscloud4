package com.baiyi.opscloud.leo.action.deploy.strategy.inspection;

import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.action.deploy.strategy.inspection.base.PreInspectionStrategy;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.helper.PodDetailsHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * 灰度预检查策略
 *
 * @Author baiyi
 * @Date 2022/12/13 15:16
 * @Version 1.0
 */
@Slf4j
@Component
public class PreInspectionWithOfflineStrategy extends PreInspectionStrategy {

    @Resource
    private PodDetailsHelper podDetailsHelper;

    /**
     * 预检查
     *
     * @param leoDeploy
     * @param deployConfig
     */
    @Override
    protected void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        preHandle(leoDeploy, deployConfig);
        LeoDeployModel.Deploy deploy = deployConfig.getDeploy();

        final String image = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .map(LeoBaseModel.Kubernetes::getDeployment)
                .map(LeoBaseModel.Deployment::getContainer)
                .map(LeoBaseModel.Container::getImage)
                .orElseThrow(() -> new LeoDeployException("未找到容器image配置！"));

        Map<String, String> dict = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getDict)
                .orElseThrow(() -> new LeoDeployException("字典配置不存在！"));

        LeoBaseModel.Kubernetes kubernetes = Optional.of(deploy)
                .map(LeoDeployModel.Deploy::getKubernetes)
                .orElseThrow(() -> new LeoDeployException("Kubernetes配置不存在！"));

        final String deploymentName = kubernetes.getDeployment().getName();
        // 校验无状态名称
        if (!deploymentName.endsWith("-canary")) {
            throw new LeoDeployException("非金丝雀环境禁止下线操作！");
        }

        LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                .id(leoDeploy.getId())
                .deployConfig(deployConfig.dump())
                .deployStatus("校验Kubernetes阶段: 成功")
                .build();
        save(saveLeoDeploy, "校验Kubernetes成功");
    }

    @Override
    public String getDeployType() {
        return DeployTypeConstants.OFFLINE.name();
    }

}
