package com.baiyi.opscloud.leo.handler.build;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.handler.build.chain.pre.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/11/14 18:00
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoBuildHandler implements InitializingBean {

    /**
     * 构建前删除历史构建任务
     */
    private final DeleteHistoricalBuildJobChainHandler deleteHistoricalBuildJobChainHandler;

    /**
     * 选举Jenkins执行实例
     */
    private final ElectInstanceChainHandler electInstanceChainHandler;

    /**
     * 在Jenkins实例中创建BuildJob
     */
    private final CreateJobChainHandler createJobChainHandler;

    /**
     * 执行BuildJob
     */
    private final DoBuildChainHandler doBuildChainHandler;

    /**
     * 执行构建通知
     */
    private final StartBuildNotificationChainHandler startBuildNotificationChainHandler;

    /**
     * 启动监视器
     */
    private final BuildingSupervisorChainHandler buildingSupervisorChainHandler;

    @Async
    public void handleBuild(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        /*
         * 使用责任链设计模式解耦代码
         */
        deleteHistoricalBuildJobChainHandler.handleRequest(leoBuild, buildConfig);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        deleteHistoricalBuildJobChainHandler
                .setNextHandler(electInstanceChainHandler)
                .setNextHandler(createJobChainHandler)
                .setNextHandler(doBuildChainHandler)
                .setNextHandler(startBuildNotificationChainHandler)
                .setNextHandler(buildingSupervisorChainHandler);
    }

}