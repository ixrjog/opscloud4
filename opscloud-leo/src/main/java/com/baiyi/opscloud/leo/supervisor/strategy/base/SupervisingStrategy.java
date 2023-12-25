package com.baiyi.opscloud.leo.supervisor.strategy.base;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO;
import com.baiyi.opscloud.leo.constants.DeployStepConstants;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.handler.deploy.base.IDeployStrategy;
import com.baiyi.opscloud.leo.helper.PodDetailsHelper;
import com.baiyi.opscloud.leo.log.LeoDeployingLog;
import com.baiyi.opscloud.leo.util.SnapshotStash;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.Date;

import static com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO.MAX_RESTART;

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

    @Resource
    protected LeoDeployingLog leoLog;

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
            if (verifyError(leoDeploy, deploying)) {
                log.error("任务错误: deployId={}", leoDeploy.getId());
            } else {
                if (verifyFinish(leoDeploy, deploying)) {
                    // 任务正常结束
                    log.info("任务完成: deployId={}", leoDeploy.getId());
                }
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
     * @return true 有错误
     */
    protected boolean verifyError(LeoDeploy leoDeploy, LeoDeployingVO.Deploying deploying) {
        if (deploying.isMaxRestartError()) {
            String message = StringFormatter.format("执行部署任务阶段: 容器重启次数超过最大值 maxRestart={}", MAX_RESTART);
            LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .endTime(new Date())
                    .deployResult("ERROR")
                    .deployStatus(message)
                    .isFinish(true)
                    .isActive(false)
                    .build();
            leoDeployService.updateByPrimaryKeySelective(saveLeoDeploy);
            leoLog.error(leoDeploy,message);
            return true;
        }
        return false;
    }

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