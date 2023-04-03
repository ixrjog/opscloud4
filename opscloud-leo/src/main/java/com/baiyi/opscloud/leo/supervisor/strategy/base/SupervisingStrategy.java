package com.baiyi.opscloud.leo.supervisor.strategy.base;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO;
import com.baiyi.opscloud.leo.action.deploy.base.IDeployStrategy;
import com.baiyi.opscloud.leo.constants.DeployStepConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.helper.PodDetailsHelper;
import com.baiyi.opscloud.leo.util.SnapshotStash;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/12/13 15:25
 * @Version 1.0
 */
@Slf4j
public abstract class SupervisingStrategy implements IDeployStrategy, InitializingBean {

    @Resource
    private SnapshotStash snapshotStash;

    @Resource
    protected PodDetailsHelper podDetailsHelper;

    @Resource
    protected LeoDeployService leoDeployService;

    @Override
    public String getStep() {
        return DeployStepConstants.SUPERVISING.name();
    }

    public void handle(LeoDeploy leoDeploy,
                       LeoDeployModel.DeployConfig deployConfig,
                       KubernetesConfig.Kubernetes kubernetes,
                       LeoDeployModel.Deploy deploy,
                       LeoBaseModel.Deployment deployment) {
        try {
            LeoDeployingVO.Deploying deploying = getDeploying(leoDeploy, deployConfig, kubernetes, deploy, deployment);
            stash(leoDeploy, deploying);
            if (verifyFinish(leoDeploy, deploying)) {
                // 任务正常结束
                log.info("任务结束: deployId={}", leoDeploy.getId());
            } else {
                verifyError(leoDeploy, deploying);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 校验任务是否结束
     *
     * @param leoDeploy
     * @param deploying
     * @return
     */
    abstract protected boolean verifyFinish(LeoDeploy leoDeploy, LeoDeployingVO.Deploying deploying);

    /**
     * 校验任务是否错误
     *
     * @param leoDeploy
     * @param deploying
     */
    abstract protected void verifyError(LeoDeploy leoDeploy, LeoDeployingVO.Deploying deploying);

    abstract protected LeoDeployingVO.Deploying getDeploying(LeoDeploy leoDeploy,
                                                             LeoDeployModel.DeployConfig deployConfig,
                                                             KubernetesConfig.Kubernetes kubernetes,
                                                             LeoDeployModel.Deploy deploy,
                                                             LeoBaseModel.Deployment deployment);

    /**
     * 存储
     *
     * @param leoDeploy
     * @param deploying
     */
    private void stash(LeoDeploy leoDeploy, LeoDeployingVO.Deploying deploying) {
        snapshotStash.save(leoDeploy.getId(), deploying);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SupervisingStrategyFactory.register(this);
    }

}
