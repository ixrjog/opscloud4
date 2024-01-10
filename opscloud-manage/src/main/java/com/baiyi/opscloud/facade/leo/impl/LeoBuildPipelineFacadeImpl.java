package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.param.leo.LeoBuildPipelineParam;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildPipelineVO;
import com.baiyi.opscloud.facade.leo.LeoBuildPipelineFacade;
import com.baiyi.opscloud.leo.domain.model.JenkinsPipeline;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.driver.BlueRestDriver;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/11/28 16:40
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoBuildPipelineFacadeImpl implements LeoBuildPipelineFacade {

    private final BlueRestDriver blueRestDriver;

    private final LeoBuildService buildService;

    private final DsConfigManager dsConfigManager;

    private static final String buildNumber = String.valueOf(1);

    @Override
    public List<LeoBuildPipelineVO.Step> getPipelineRunNodeSteps(LeoBuildPipelineParam.GetPipelineRunNodeSteps param) {
        LeoBuild leoBuild = buildService.getById(param.getBuildId());
        if (leoBuild.getIsDeletedBuildJob()) {
            return Collections.emptyList();
        }
        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.load(leoBuild);
        LeoBaseModel.DsInstance dsInstance = buildConfig.getBuild().getJenkins().getInstance();
        JenkinsConfig jenkinsConfig = getJenkinsConfigWithUuid(dsInstance.getUuid());
        try {
            List<JenkinsPipeline.Step> steps = blueRestDriver.getPipelineNodeSteps(
                    jenkinsConfig.getJenkins(),
                    leoBuild.getBuildJobName(),
                    String.valueOf(1),
                    param.getNodeId());
            // 加入步骤日志
            return steps.stream().map(s -> toStepVO(jenkinsConfig, leoBuild, param.getNodeId(), s)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new LeoBuildException("Error querying pipeline task deleted ！");
        }
    }

    private LeoBuildPipelineVO.Step toStepVO(JenkinsConfig jenkinsConfig, LeoBuild leoBuild, String nodeId, JenkinsPipeline.Step step) {
        LeoBuildPipelineVO.Step stepVO = BeanCopierUtil.copyProperties(step, LeoBuildPipelineVO.Step.class);
        try {
            final String stepLog = blueRestDriver.getPipelineNodeStepLog(
                    jenkinsConfig.getJenkins(),
                    leoBuild.getBuildJobName(),
                    buildNumber,
                    nodeId,
                    stepVO.getId());
            stepVO.setLog(stepLog);
        } catch (Exception ignore) {
        }
        return stepVO;
    }

    private JenkinsConfig getJenkinsConfigWithUuid(String uuid) {
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(uuid);
        return dsConfigManager.build(dsConfig, JenkinsConfig.class);
    }

}