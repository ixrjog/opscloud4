package com.baiyi.opscloud.leo.handler.build.chain.post;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.leo.handler.build.BaseBuildChainHandler;
import com.baiyi.opscloud.leo.converter.JenkinsPipelineConverter;
import com.baiyi.opscloud.leo.domain.model.JenkinsPipeline;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.driver.BlueRestDriver;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/28 13:31
 * @Version 1.0
 */
@Slf4j
@Component
public class RecordBuildPipelineChainHandler extends BaseBuildChainHandler {

    @Resource
    private BlueRestDriver blueRestDriver;

    /**
     * 记录流水线
     *
     * @param leoBuild
     * @param buildConfig
     */
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        try {
            LeoBaseModel.DsInstance dsInstance = buildConfig.getBuild().getJenkins().getInstance();
            JenkinsConfig jenkinsConfig = getJenkinsConfigWithUuid(dsInstance.getUuid());
            List<JenkinsPipeline.Node> nodes = blueRestDriver.getPipelineNodes(jenkinsConfig.getJenkins(), leoBuild.getBuildJobName(), String.valueOf(1));
            LeoBuildVO.Pipeline pipeline = LeoBuildVO.Pipeline.builder()
                    .nodes(JenkinsPipelineConverter.toLeoBuildNodes(nodes))
                    .build();

            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .buildStatus("结束构建记录流水线阶段: 成功")
                    .pipelineContent(JSONUtil.writeValueAsString(pipeline))
                    .build();
            leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
            leoLog.info(leoBuild, "结束构建记录流水线成功");
        } catch (LeoBuildException e) {
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .buildStatus("结束构建记录流水线成功失败")
                    .build();
            leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
            throw new LeoBuildException("结束构建记录流水线成功失败: {}", e.getMessage());
        }
    }

}