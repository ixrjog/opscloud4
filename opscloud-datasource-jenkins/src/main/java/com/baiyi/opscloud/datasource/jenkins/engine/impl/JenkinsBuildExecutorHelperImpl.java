package com.baiyi.opscloud.datasource.jenkins.engine.impl;

import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.jenkins.driver.JenkinsServerDriver;
import com.baiyi.opscloud.datasource.jenkins.engine.JenkinsBuildExecutorHelper;
import com.baiyi.opscloud.datasource.jenkins.model.Computer;
import com.baiyi.opscloud.datasource.jenkins.model.ComputerWithDetails;
import com.baiyi.opscloud.datasource.jenkins.model.Executor;
import com.baiyi.opscloud.datasource.jenkins.model.Job;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.datasource.jenkins.status.JenkinsBuildExecutorStatusVO;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/8/1 10:01
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JenkinsBuildExecutorHelperImpl implements JenkinsBuildExecutorHelper {

    public final static String INACTIVE = "(Inactive)";

    public final static String BUILT_IN_NODE = "built-in node";

    public final static String IDLE = "Idle";

    private final RedisUtil redisUtil;

    private final DsConfigHelper dsConfigHelper;

    @Override
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_10S, key = "'jenkins.build.executor#instance_uuid_'+ #instance.uuid", unless = "#result == null")
    public JenkinsBuildExecutorStatusVO.Children generatorBuildExecutorStatus(DatasourceInstance instance) {
        DatasourceConfig datasourceConfig = dsConfigHelper.getConfigByInstanceUuid(instance.getUuid());
        JenkinsConfig jenkinsConfig = dsConfigHelper.build(datasourceConfig, JenkinsConfig.class);
        List<JenkinsBuildExecutorStatusVO.Children> children = buildComputers(jenkinsConfig.getJenkins());
        return JenkinsBuildExecutorStatusVO.Children.builder()
                .name(StringUtils.isNoneBlank(jenkinsConfig.getJenkins().getName()) ? jenkinsConfig.getJenkins().getName() : instance.getInstanceName())
                .children(children)
                .value(children.size())
                .build();
    }

    private List<JenkinsBuildExecutorStatusVO.Children> buildComputers(JenkinsConfig.Jenkins jenkins) {
        List<JenkinsBuildExecutorStatusVO.Children> computers = Lists.newArrayList();
        try {
            Map<String, Computer> computerMap = JenkinsServerDriver.getComputers(jenkins);
            computerMap.keySet().forEach(k -> {
                if (!k.equals(BUILT_IN_NODE)) {
                    List<JenkinsBuildExecutorStatusVO.Children> executors = buildExecutors(computerMap.get(k));
                    Computer computer = computerMap.get(k);
                    String name = k;
                    try {
                        if (computer.details().getOffline())
                            name += "(Offline)";
                    } catch (IOException e) {
                        log.error("查询节点状态错误: name={}, err= {}", k, e.getMessage());
                    }
                    JenkinsBuildExecutorStatusVO.Children node = JenkinsBuildExecutorStatusVO.Children.builder()
                            .name(name)
                            .children(executors)
                            .value(executors.size())
                            .build();
                    computers.add(node);
                }
            });
        } catch (Exception e) {
            log.error("组装Jenkins引擎工作负载错误: err={}", e.getMessage());
        }
        return computers;
    }

    private List<JenkinsBuildExecutorStatusVO.Children> buildExecutors(Computer computer) {
        List<JenkinsBuildExecutorStatusVO.Children> executors = Lists.newArrayList();
        try {
            ComputerWithDetails computerWithDetails = computer.details();
            computerWithDetails.getExecutors().forEach(e -> {
                JenkinsBuildExecutorStatusVO.Children executor = JenkinsBuildExecutorStatusVO.Children.builder()
                        .name(acqExecutorName(e))
                        .value(1)
                        .build();
                executors.add(executor);
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return executors;
    }

    private String acqExecutorName(Executor executor) {
        if (executor.getCurrentExecutable() != null) {
            Job job = executor.getCurrentExecutable();
            //   JobBuild jobBuild = JobBuildUtils.convert(job.getUrl());
            //   return jobBuild.getJobName();
            return job.getUrl();
        } else {
            return IDLE;
        }
    }

}
