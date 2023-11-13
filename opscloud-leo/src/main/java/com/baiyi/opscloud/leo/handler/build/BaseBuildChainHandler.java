package com.baiyi.opscloud.leo.handler.build;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.log.LeoBuildingLog;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import jakarta.annotation.Resource;
import lombok.Getter;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/11/14 17:06
 * @Version 1.0
 */
public abstract class BaseBuildChainHandler {

    public static final String RESULT_ERROR = "ERROR";

    @Resource
    protected DsConfigManager dsConfigManager;

    @Resource
    protected LeoBuildingLog leoLog;

    @Resource
    protected LeoBuildService leoBuildService;

    @Getter
    private BaseBuildChainHandler next;

    protected JenkinsConfig getJenkinsConfigWithUuid(String uuid) {
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(uuid);
        return dsConfigManager.build(dsConfig, JenkinsConfig.class);
    }

    public BaseBuildChainHandler setNextHandler(BaseBuildChainHandler next) {
        this.next = next;
        return this.next;
    }

    public void handleRequest(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        try {
            this.handle(leoBuild, buildConfig);
        } catch (LeoBuildException e) {
            // 记录日志
            leoLog.error(leoBuild, e.getMessage());
            LeoBuild saveLeoBuild =LeoBuild.builder()
                    .id(leoBuild.getId())
                    .buildResult(RESULT_ERROR)
                    .endTime(new Date())
                    .isFinish(true)
                    .buildStatus(e.getMessage())
                    .isActive(false)
                    .build();
            save(saveLeoBuild);
            throw e;
        }
        if (getNext() != null) {
            getNext().handleRequest(leoBuildService.getById(leoBuild.getId()), buildConfig);
        }
    }

    /**
     * 抽象方法，具体实现
     *
     * @param leoBuild
     * @param buildConfig
     */
    protected abstract void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig);

    protected void save(LeoBuild saveLeoBuild) {
        leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
    }

    protected void save(LeoBuild saveLeoBuild, String log, Object... var2) {
        leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
        leoLog.info(saveLeoBuild, log, var2);
    }

}