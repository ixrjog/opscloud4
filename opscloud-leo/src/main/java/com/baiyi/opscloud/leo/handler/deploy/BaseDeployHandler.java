package com.baiyi.opscloud.leo.handler.deploy;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.exception.LeoDeployException;
import com.baiyi.opscloud.leo.helper.DeployingLogHelper;
import com.baiyi.opscloud.service.leo.LeoDeployService;

import jakarta.annotation.Resource;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/12/5 19:55
 * @Version 1.0
 */
public abstract class BaseDeployHandler {

    public static final String RESULT_ERROR = "ERROR";

    @Resource
    protected DsConfigHelper dsConfigHelper;

    @Resource
    protected DeployingLogHelper logHelper;

    @Resource
    protected LeoDeployService deployService;

    private BaseDeployHandler next;

    protected KubernetesConfig getKubernetesConfigWithUuid(String uuid) {
        DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(uuid);
        return dsConfigHelper.build(dsConfig, KubernetesConfig.class);
    }

    public BaseDeployHandler setNextHandler(BaseDeployHandler next) {
        this.next = next;
        return this.next;
    }

    public BaseDeployHandler getNext() {
        return next;
    }

    public void handleRequest(LeoDeploy leoDeploy, LeoDeployModel.DeployConfig deployConfig) {
        try {
            this.handle(leoDeploy, deployConfig);
        } catch (Exception e) {
            // 记录日志
            logHelper.error(leoDeploy, e.getMessage());
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
        logHelper.info(saveLeoDeploy, log, var2);
        deployService.updateByPrimaryKeySelective(saveLeoDeploy);
    }

}