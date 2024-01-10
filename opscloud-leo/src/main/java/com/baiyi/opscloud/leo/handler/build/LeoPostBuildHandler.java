package com.baiyi.opscloud.leo.handler.build;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.handler.build.chain.post.BuildFinalProcessingChainHandler;
import com.baiyi.opscloud.leo.handler.build.chain.post.EndBuildNotificationChainHandler;
import com.baiyi.opscloud.leo.handler.build.chain.post.PostBuildVerificationChainHandler;
import com.baiyi.opscloud.leo.handler.build.chain.post.RecordBuildPipelineChainHandler;
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

    /**
     * 构建结束后校验
     */
    private final PostBuildVerificationChainHandler postBuildVerificationChainHandler;

    /**
     * 构建结束通知
     */
    private final EndBuildNotificationChainHandler endBuildNotificationChainHandler;

    /**
     * 记录流水线日志
     */
    private final RecordBuildPipelineChainHandler recordBuildPipelineChainHandler;

    /**
     * 构建最后处理
     */
    private final BuildFinalProcessingChainHandler buildFinalProcessingChainHandler;

    @Async
    public void handleBuild(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        /*
          使用责任链设计模式解耦代码
         */
        postBuildVerificationChainHandler.handleRequest(leoBuild, buildConfig);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        postBuildVerificationChainHandler
                .setNextHandler(endBuildNotificationChainHandler)
                .setNextHandler(recordBuildPipelineChainHandler)
                .setNextHandler(buildFinalProcessingChainHandler);
    }

}