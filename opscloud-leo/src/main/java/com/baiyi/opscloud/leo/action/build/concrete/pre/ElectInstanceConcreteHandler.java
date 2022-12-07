package com.baiyi.opscloud.leo.action.build.concrete.pre;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsServerDriver;
import com.baiyi.opscloud.datasource.jenkins.helper.JenkinsVersion;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.action.build.BaseBuildHandler;
import com.baiyi.opscloud.leo.action.build.helper.LeoJenkinsInstanceHelper;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @Author baiyi
 * @Date 2022/11/14 17:10
 * @Version 1.0
 */
@Slf4j
@Component
public class ElectInstanceConcreteHandler extends BaseBuildHandler {

    @Resource
    private LeoJenkinsInstanceHelper leoJenkinsInstanceHelper;

    /**
     * 选举Jenkins实例
     *
     * @param leoBuild
     * @param buildConfig
     */
    @Override
    protected void handle(LeoBuild leoBuild, LeoBuildModel.BuildConfig buildConfig) {
        List<String> jobTags = buildConfig.getBuild().getTags();
        List<DatasourceInstance> instances = electLeoJenkinsInstances(jobTags);
        List<DatasourceInstance> activeInstances = Lists.newArrayList();
        instances.forEach(instance -> {
            JenkinsConfig jenkinsConfig = getJenkinsConfigWithUuid(instance.getUuid());
            try {
                JenkinsVersion jenkinsVersion = JenkinsServerDriver.getVersion(jenkinsConfig.getJenkins());
                if (jenkinsVersion != null && StringUtils.isNotBlank(jenkinsVersion.getLiteralVersion()))
                    activeInstances.add(instance);
            } catch (URISyntaxException | IOException e) {
                log.warn("查询Jenkins实例状态错误: err={}", e.getMessage());
            }
        });

        if (CollectionUtils.isEmpty(activeInstances)) {
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .endTime(new Date())
                    .isFinish(true)
                    .buildResult(BUILD_RESULT_ERROR)
                    .buildStatus("选举实例阶段")
                    .build();
            save(saveLeoBuild);
            throw new LeoBuildException("无可用的Jenkins实例！");
        }

        DatasourceInstance activeInstance = getOneInstance(activeInstances);
        LeoBaseModel.DsInstance dsInstance = LeoBaseModel.DsInstance.builder()
                .name(activeInstance.getInstanceName())
                .uuid(activeInstance.getUuid())
                .build();

        LeoBaseModel.Jenkins jenkins = LeoBaseModel.Jenkins.builder()
                .instance(dsInstance)
                .build();

        buildConfig.getBuild().setJenkins(jenkins);
        LeoBuild saveLeoBuild = LeoBuild.builder()
                .id(leoBuild.getId())
                .buildStatus("选举实例阶段: 成功")
                .buildConfig(buildConfig.dump())
                .build();
        leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
        save(saveLeoBuild, "选举Jenkins实例: name={}, uuid={}", dsInstance.getName(), dsInstance.getUuid());
    }

    private List<DatasourceInstance> electLeoJenkinsInstances(List<String> tags) {
        List<DatasourceInstance> instances = leoJenkinsInstanceHelper.queryAvailableInstancesWithTags(tags);
        if (CollectionUtils.isEmpty(instances))
            throw new LeoBuildException("无可用的Jenkins实例: tags={}", JSONUtil.writeValueAsString(tags));
        return instances;
    }

    /**
     * 随机选取一个可用实例
     *
     * @param activeInstances
     * @return
     */
    private DatasourceInstance getOneInstance(List<DatasourceInstance> activeInstances) {
        if (activeInstances.size() == 1)
            return activeInstances.get(0);
        Random random = new Random();
        int n = random.nextInt(activeInstances.size());
        return activeInstances.get(n);
    }

}
