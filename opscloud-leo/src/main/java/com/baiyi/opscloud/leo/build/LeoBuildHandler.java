package com.baiyi.opscloud.leo.build;

import com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.build.concrete.*;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/11/14 18:00
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoBuildHandler implements InitializingBean {

    // 选举Jenkins执行实例
    private final ElectInstanceConcreteHandler electInstanceConcreteHandler;

    // 在Jenkins实例中创建BuildJob
    private final CreateJobConcreteHandler createJobConcreteHandler;

    // 执行BuildJob
    private final DoBuildConcreteHandler doBuildConcreteHandler;

    // 启动监视器
    private final BuildingSupervisorConcreteHandler buildingSupervisorConcreteHandler;

    // 构建后通知
    private final PostBuildNotificationConcreteHandler postBuildNotificationConcreteHandler;

    @Async(value = ThreadPoolTaskConfiguration.TaskPools.CORE)
    public void buildHandle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        // 使用责任链设计模式解耦代码
        electInstanceConcreteHandler.handleRequest(leoBuild, buildConfig);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        electInstanceConcreteHandler.setNextHandler(createJobConcreteHandler);
        createJobConcreteHandler.setNextHandler(doBuildConcreteHandler);
        doBuildConcreteHandler.setNextHandler(buildingSupervisorConcreteHandler);
        buildingSupervisorConcreteHandler.setNextHandler(postBuildNotificationConcreteHandler);
    }

}
