package com.baiyi.opscloud.leo.handler.build.chain.pre;

import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsServerDriver;
import com.baiyi.opscloud.datasource.jenkins.helper.JenkinsVersion;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.leo.domain.model.JenkinsInstanceTask;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import com.baiyi.opscloud.leo.exception.LeoBuildException;
import com.baiyi.opscloud.leo.handler.build.BaseBuildChainHandler;
import com.baiyi.opscloud.leo.handler.build.helper.LeoJenkinsInstanceHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.base.Global.AUTO_BUILD;
import static com.baiyi.opscloud.common.base.Global.AUTO_DEPLOY;

/**
 * @Author baiyi
 * @Date 2022/11/14 17:10
 * @Version 1.0
 */
@Slf4j
@Component
public class ElectInstanceChainHandler extends BaseBuildChainHandler {

    private final static Set<String> FILTER_TAGS_SET = Sets.newHashSet(AUTO_BUILD, AUTO_DEPLOY);

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
                if (jenkinsVersion != null && StringUtils.isNotBlank(jenkinsVersion.getLiteralVersion())) {
                    activeInstances.add(instance);
                }
            } catch (URISyntaxException | IOException e) {
                log.warn("查询Jenkins实例状态错误: {}", e.getMessage());
            }
        });

        if (CollectionUtils.isEmpty(activeInstances)) {
            LeoBuild saveLeoBuild = LeoBuild.builder()
                    .id(leoBuild.getId())
                    .endTime(new Date())
                    .isFinish(true)
                    .buildResult(RESULT_ERROR)
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

        buildConfig.getBuild()
                .setJenkins(jenkins);

        LeoBuild saveLeoBuild = LeoBuild.builder()
                .id(leoBuild.getId())
                .buildStatus("选举实例阶段: 成功")
                .buildConfig(buildConfig.dump())
                .build();
        leoBuildService.updateByPrimaryKeySelective(saveLeoBuild);
        save(saveLeoBuild, "选举Jenkins实例: name={}, uuid={}", dsInstance.getName(), dsInstance.getUuid());
    }

    private List<DatasourceInstance> electLeoJenkinsInstances(List<String> jobTags) {
        // 过滤AutoBuild、AutoDeploy标签
        List<String> tags = jobTags.stream().filter(t -> !FILTER_TAGS_SET.contains(t)).collect(Collectors.toList());
        List<DatasourceInstance> instances = leoJenkinsInstanceHelper.queryAvailableInstancesWithTags(tags);
        if (CollectionUtils.isEmpty(instances)) {
            throw new LeoBuildException("无可用的Jenkins实例: tags={}", JSONUtil.writeValueAsString(tags));
        }
        return instances;
    }

    /**
     * 取任务最少的实例
     *
     * @param activeInstances
     * @return
     */
    private DatasourceInstance getOneInstance(List<DatasourceInstance> activeInstances) {
        if (activeInstances.size() == 1) {
            return activeInstances.getFirst();
        }
        List<LeoBuild> builds = leoBuildService.queryNotFinishBuild();
        // 所有引擎都空闲
        if (CollectionUtils.isEmpty(builds)) {
            return getRandomOneInstance(activeInstances);
        }
        // 计算任务最少的实例
        try {
            Map<String, JenkinsInstanceTask.Task> taskMap = Maps.newHashMap();
            int i = 0, activeInstancesSize = activeInstances.size();
            while (i < activeInstancesSize) {
                DatasourceInstance activeInstance = activeInstances.get(i);
                JenkinsInstanceTask.Task task = JenkinsInstanceTask.Task.builder()
                        .name(activeInstance.getInstanceName())
                        .index(i)
                        .task(0)
                        .build();
                taskMap.put(task.getName(), task);
                i++;
            }

            builds.forEach(build -> {
                LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.load(build);
                Optional<String> optionalInstanceName = Optional.ofNullable(buildConfig)
                        .map(LeoBuildModel.BuildConfig::getBuild)
                        .map(LeoBuildModel.Build::getJenkins)
                        .map(LeoBaseModel.Jenkins::getInstance)
                        .map(LeoBaseModel.DsInstance::getName);
                if (optionalInstanceName.isPresent()) {
                    final String instanceName = optionalInstanceName.get();
                    if (taskMap.containsKey(instanceName)) {
                        taskMap.get(instanceName).setTask(taskMap.get(instanceName).getTask() + 1);
                    }
                }
            });

            List<JenkinsInstanceTask.Task> tasks = taskMap.keySet().stream().map(taskMap::get).collect(Collectors.toList());
            JenkinsInstanceTask jenkinsInstanceTask = JenkinsInstanceTask.builder()
                    .instanceTasks(tasks)
                    .build();
            jenkinsInstanceTask.sort();
            return activeInstances.get(jenkinsInstanceTask.getIndex());
        } catch (Exception e) {
            log.error("Leo selected build engine instance error: {}", e.getMessage());
            return getRandomOneInstance(activeInstances);
        }
    }

    /**
     * 随机选择一个实例
     *
     * @param activeInstances
     * @return
     */
    private DatasourceInstance getRandomOneInstance(List<DatasourceInstance> activeInstances) {
        Collections.shuffle(activeInstances);
        return activeInstances.getFirst();
    }

}