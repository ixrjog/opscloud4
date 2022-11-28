package com.baiyi.opscloud.leo.build;

import com.baiyi.opscloud.common.config.ThreadPoolTaskConfiguration;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.build.concrete.post.EndBuildNotificationConcreteHandler;
import com.baiyi.opscloud.leo.build.concrete.post.RecordBuildPipelineConcreteHandler;
import com.baiyi.opscloud.leo.build.concrete.post.VerifyKubernetesImageConcreteHandler;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/11/17 10:24
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoPostBuildHandler implements InitializingBean {

    // 验证 Kubernetes Image
    private final VerifyKubernetesImageConcreteHandler verifyKubernetesImageConcreteHandler;

    // 构建结束通知
    private final EndBuildNotificationConcreteHandler endBuildNotificationConcreteHandler;

    // 记录流水线日志
    private final RecordBuildPipelineConcreteHandler recordBuildPipelineConcreteHandler;

    @Async(value = ThreadPoolTaskConfiguration.TaskPools.CORE)
    public void buildHandle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        // 使用责任链设计模式解耦代码
        verifyKubernetesImageConcreteHandler.handleRequest(leoBuild, buildConfig);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        verifyKubernetesImageConcreteHandler
                .setNextHandler(endBuildNotificationConcreteHandler)
                .setNextHandler(recordBuildPipelineConcreteHandler);
    }

}
