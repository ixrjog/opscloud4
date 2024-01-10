package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.facade.leo.LeoChartFacade;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/12/9 13:45
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeoChartFacadeImpl implements LeoChartFacade {

    private final ApplicationService applicationService;

    private final LeoBuildService leoBuildService;

    private final LeoDeployService leoDeployService;

    private final LeoJobService leoJobService;

    @Override
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1D, unless = "#result == null")
    public Map<String, Integer> getKeywords() {
        return prod();
    }

    private Map<String, Integer> test(){
        Map<String, Integer> keywords = Maps.newHashMap();
        List<Application> applications = applicationService.queryAll();
        applications.forEach(a -> keywords.put(a.getName(), 10000));
        return keywords;
    }

    private Map<String, Integer> prod(){
        Map<String, Integer> keywords = Maps.newHashMap();
        List<Application> applications = applicationService.queryAll();
        applications.forEach(a -> {
            int count = 1;
            List<LeoJob> jobs = leoJobService.queryJobWithApplicationId(a.getId());
            if (!CollectionUtils.isEmpty(jobs)) {
                for (LeoJob job : jobs) {
                    count = leoBuildService.countWithJobId(job.getId()) + count;
                    count = leoDeployService.countWithJobId(job.getId()) + count;
                }
            }
            keywords.put(a.getName(), count);
        });
        return keywords;
    }

}