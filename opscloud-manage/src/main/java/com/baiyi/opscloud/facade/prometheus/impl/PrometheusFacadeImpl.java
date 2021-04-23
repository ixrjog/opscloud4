package com.baiyi.opscloud.facade.prometheus.impl;

import com.baiyi.opscloud.bo.prometheus.PrometheusBO;
import com.baiyi.opscloud.common.config.PrometheusConfig;
import com.baiyi.opscloud.common.util.*;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcFileTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.param.prometheus.PrometheusParam;
import com.baiyi.opscloud.facade.prometheus.PrometheusFacade;
import com.baiyi.opscloud.factory.attribute.impl.PrometheusAttribute;
import com.baiyi.opscloud.server.facade.ServerAttributeFacade;
import com.baiyi.opscloud.service.file.OcFileTemplateService;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/2/24 5:08 下午
 * @Version 1.0
 */
@Service
@Slf4j
public class PrometheusFacadeImpl implements PrometheusFacade {

    @Resource
    private PrometheusConfig prometheusConfig;

    @Resource
    private ServerAttributeFacade serverAttributeFacade;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcFileTemplateService ocFileTemplateService;

    @Resource
    private PrometheusAttribute prometheusAttribute;

    private static final String PROMETHEUS_JOB = "PROMETHEUS_JOB";

    private static final String PROMETHEUS_CONFIG = "PROMETHEUS_CONFIG";

    @Override
    public Map<String, List<PrometheusBO.Target>> getTargetMap() {
        List<PrometheusBO.Job> jobs = getJobs();
        Map<String, List<PrometheusBO.Target>> map = Maps.newHashMap();
        jobs.forEach(e -> {
            List<PrometheusBO.Target> targets = prometheusAttribute.buildTargets(e);
            map.put(e.getServerGroup().getName(), targets);
        });
        return map;
    }

    @Override
    public BusinessWrapper<String> getPrometheusJobTemplate() {
        List<PrometheusBO.Job> jobs = getJobs();
        return getPrometheusJobTemplate(jobs);
    }


    @Override
    public BusinessWrapper<String> getGroupTarget(Integer serverGroupId) {
        List<PrometheusBO.Job> jobs = getJob(serverGroupId);
        if (CollectionUtils.isEmpty(jobs))
            return new BusinessWrapper<>(Strings.EMPTY);
        List<PrometheusBO.Target> targets = prometheusAttribute.buildTargets(jobs.get(0));
        String target = JSONFormat.format(targets);
        return new BusinessWrapper<>(target);
    }

    @Override
    public BusinessWrapper<String> getPrometheusJobTemplate(Integer serverGroupId) {
        List<PrometheusBO.Job> jobs = getJob(serverGroupId);
        if (CollectionUtils.isEmpty(jobs))
            return new BusinessWrapper<>(Strings.EMPTY);
        return getPrometheusJobTemplate(jobs);
    }

    private BusinessWrapper<String> getPrometheusJobTemplate(List<PrometheusBO.Job> jobs) {
        Map<String, Object> contentMap = Maps.newHashMap();
        contentMap.put("jobs", jobs);
        OcFileTemplate template =
                ocFileTemplateService.queryOcFileTemplateByUniqueKey(PROMETHEUS_JOB, 0);
        try {
            return new BusinessWrapper<>(BeetlUtils.renderTemplate(template.getContent(), contentMap));
        } catch (IOException e) {
            log.error("渲染PROMETHEUS_JOB模板失败!", e);
            return new BusinessWrapper<>(ErrorEnum.PROMETHEUS_TEMP_RENDER_FAIL);
        }
    }

    @Override
    public List<PrometheusBO.Job> getJobs() {
        List<OcServerGroup> serverGroups = ocServerGroupService.queryAll();
        return getJobs(serverGroups);
    }

    private List<PrometheusBO.Job> getJob(Integer serverGroupId) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(serverGroupId);
        return getJobs(Lists.newArrayList(ocServerGroup));
    }

    private List<PrometheusBO.Job> getJobs(List<OcServerGroup> serverGroups) {
        List<PrometheusBO.Job> jobs = Lists.newArrayList();
        serverGroups.forEach(e -> {
            Map<String, String> attributeMap = serverAttributeFacade.getServerGroupAttributeMap(e);
            if (PrometheusUtils.isEnable(attributeMap))
                jobs.add(buildJob(e, attributeMap));
        });
        return jobs;
    }

    @Override
    public BusinessWrapper<Boolean> createPrometheusConfigTask() {
        writeConfig();
        writeConfigFiles();
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public void writeConfig() {
        String prometheusYml = Joiner.on("/").join(prometheusConfig.getDataPath(), "prometheus.yml");
        OcFileTemplate template = ocFileTemplateService.queryOcFileTemplateByUniqueKey(PROMETHEUS_CONFIG, 0);
        String tpl = Joiner.on("\n\n").join(template.getContent(), getPrometheusJobTemplate().getBody());
        IOUtils.writeFile(tpl, prometheusYml);
    }

    @Override
    public void writeConfigFiles() {
        Map<String, List<PrometheusBO.Target>> targetMap = getTargetMap();
        targetMap.keySet().forEach(k -> {
            List<PrometheusBO.Target> targets = targetMap.get(k);
            String file = prometheusConfig.buildConfigFilePath(targets.get(0).getLabels().getApplication());
            IOUtils.writeFile(JSONFormat.format(targets), Joiner.on(".").join(file, "json"));
        });
    }

    private PrometheusBO.Job buildJob(OcServerGroup ocServerGroup, Map<String, String> attributeMap) {
        String name = ServerGroupUtils.getShortName(ocServerGroup);
        return PrometheusBO.Job.builder()
                .serverGroup(ocServerGroup)
                .jobName(PrometheusUtils.getJobName(name, attributeMap))
                .metricsPath(PrometheusUtils.getMetricsPath(name, attributeMap))
                .configPath(PrometheusUtils.getConfigPath(name, attributeMap))
                .comment(Joiner.on(" ").skipNulls().join(ocServerGroup.getName(), ocServerGroup.getComment()))
                .build();
    }

    @Override
    public BusinessWrapper<Boolean> savePrometheusConfig(PrometheusParam.SaveConfig param) {
        OcFileTemplate template = ocFileTemplateService.queryOcFileTemplateByUniqueKey(PROMETHEUS_CONFIG, 0);
        template.setContent(param.getContent());
        ocFileTemplateService.updateOcFileTemplate(template);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<String> queryPrometheusConfig() {
        OcFileTemplate template = ocFileTemplateService.queryOcFileTemplateByUniqueKey(PROMETHEUS_CONFIG, 0);
        return new BusinessWrapper<>(template.getContent());
    }
}
