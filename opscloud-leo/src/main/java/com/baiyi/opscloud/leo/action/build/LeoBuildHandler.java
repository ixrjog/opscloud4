package com.baiyi.opscloud.leo.action.build;

import com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.action.build.concrete.pre.*;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
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
    private final DeleteHistoricalBuildJobConcreteHandler deleteHistoricalBuildJobConcreteHandler;

    /**
     * 选举Jenkins执行实例
     */
    private final ElectInstanceConcreteHandler electInstanceConcreteHandler;

    /**
     * 在Jenkins实例中创建BuildJob
     */
    private final CreateJobConcreteHandler createJobConcreteHandler;

    /**
     * 执行BuildJob
     */
    private final DoBuildConcreteHandler doBuildConcreteHandler;

    /**
     * 执行构建通知
     */
    private final StartBuildNotificationConcreteHandler startBuildNotificationConcreteHandler;

    /**
     * 启动监视器
     */
    private final BuildingSupervisorConcreteHandler buildingSupervisorConcreteHandler;

    @Async(value = ThreadPoolTaskConfiguration.TaskPools.CORE)
    public void handleBuild(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        /*
         * 使用责任链设计模式解耦代码
         */
        deleteHistoricalBuildJobConcreteHandler.handleRequest(leoBuild, buildConfig);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        deleteHistoricalBuildJobConcreteHandler
                .setNextHandler(electInstanceConcreteHandler)
                .setNextHandler(createJobConcreteHandler)
                .setNextHandler(doBuildConcreteHandler)
                .setNextHandler(startBuildNotificationConcreteHandler)
                .setNextHandler(buildingSupervisorConcreteHandler);
    }

}
