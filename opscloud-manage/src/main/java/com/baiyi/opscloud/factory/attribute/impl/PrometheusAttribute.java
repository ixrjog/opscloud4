package com.baiyi.opscloud.factory.attribute.impl;

import com.baiyi.opscloud.bo.prometheus.PrometheusBO;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/2/25 2:16 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class PrometheusAttribute extends BaseAttribute {

    private static final String PORT = "8080";

    public List<PrometheusBO.Target> buildTargets(PrometheusBO.Job job) {
        Map<String, List<OcServer>> serverEnvMap = groupingByEnv(job.getServerGroup());
        List<PrometheusBO.Target> targets = Lists.newArrayList();
        serverEnvMap.keySet().forEach(k ->
                targets.add(buildTarget(job, k, serverEnvMap.get(k)))
        );
        return targets;
    }

    @Override
    /**
     * ddd-prod
     *
     * @param ocServerGroup
     * @param envType
     * @return
     */
    protected String convertSubgroupName(OcServerGroup ocServerGroup, int envType) {
        return  getEnvName(envType);
    }

    private PrometheusBO.Target buildTarget(PrometheusBO.Job job, String group, List<OcServer> servers) {

        List<String> targets = servers.stream().map(s ->
                Joiner.on(":").join(s.getPrivateIp(), PORT)
        ).collect(Collectors.toList());

        PrometheusBO.Labels labels = PrometheusBO.Labels.builder()
                .application(job.getJobName())
                // 按环境分组
                .group(group)
                .build();

        return PrometheusBO.Target.builder()
                .labels(labels)
                .targets(targets)
                .build();
    }

}
