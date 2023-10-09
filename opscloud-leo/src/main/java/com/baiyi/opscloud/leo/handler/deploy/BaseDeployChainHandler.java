package com.baiyi.opscloud.leo.handler.deploy;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.log.LeoDeployingLog;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import jakarta.annotation.Resource;
import lombok.Getter;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/12/5 19:55
 * @Version 1.0
 */
public abstract class BaseDeployChainHandler {

    public static final String RESULT_ERROR = "ERROR";

    @Resource
    protected DsConfigManager dsConfigManager;

    @Resource
    protected LeoDeployingLog leoLog;

    @Resource
    protected LeoDeployService deployService;

    @Getter
    private BaseDeployChainHandler next;

    protected KubernetesConfig getKubernetesConfigWithUuid(String uuid) {
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(uuid);
        return dsConfigManager.build(dsConfig, KubernetesConfig.class);
    }

    public BaseDeployChainHandler setNextHandler(BaseDeployChainHandler next) {
        this.next = next;
        return this.next;
    }

    public void handleRequest(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        try {
            this.handle(leoDeploy, deployConfig);
        } catch (Exception e) {
            // 记录日志
            leoLog.error(leoDeploy, e.getMessage());
            LeoDeploy saveLeoDeploy = LeoDeploy.builder()
                    .id(leoDeploy.getId())
                    .deployResult(RESULT_ERROR)
                    .endTime(new Date())
                    .isFinish(true)
                    .isActive(false)
                    .deployStatus(e.getMessage())
                    .build();
            save(saveLeoDeploy);
            if (e instanceof LeoDeployException) {
                throw e;
            } else {
                throw new LeoDeployException(e.getMessage());
            }
        }
        if (getNext() != null) {
            getNext().handleRequest(deployService.getById(leoDeploy.getId()), deployConfig);
        }
    }

    /**
     * 抽象方法，具体实现
     *
     * @param leoDeploy
     * @param deployConfig
     */
    protected abstract void handle(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig);

    protected void save(LeoDeploy saveLeoDeploy) {
        deployService.updateByPrimaryKeySelective(saveLeoDeploy);
    }

    protected void save(LeoDeploy saveLeoDeploy, String log, Object... var2) {
        leoLog.info(saveLeoDeploy, log, var2);
        deployService.updateByPrimaryKeySelective(saveLeoDeploy);
    }

}