package com.baiyi.opscloud.leo.delegate;

import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.sys.EnvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/2/22 19:50
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoBuildDeploymentDelegate {

    private final LeoJobService jobService;

    private final ApplicationResourceService applicationResourceService;

    private final EnvService envService;

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_10S, key = "'leo#query#build#deployment#jobId=' + #jobId", unless = "#result == null")
    public List<ApplicationResourceVO.BaseResource> queryLeoBuildDeployment(int jobId) {
        LeoJob leoJob = jobService.getById(jobId);
        Env env = envService.getByEnvType(leoJob.getEnvType());
        List<ApplicationResource> resources = applicationResourceService.queryByApplication(
                leoJob.getApplicationId(),
                DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name(),
                BusinessTypeEnum.ASSET.getType());
        if (CollectionUtils.isEmpty(resources))
            return Collections.emptyList();
        final String envName = env.getEnvName();
        List<ApplicationResource> result = resources.stream().filter(e -> {
            if (e.getName().startsWith(envName + ":")) {
                return true;
            }
            // TODO 环境标准化后以下代码可以删除
            if (env.getEnvName().equals("dev")) {
                return e.getName().startsWith("ci:");
            }
            if (env.getEnvName().equals("daily")) {
                return e.getName().startsWith("test:");
            }
            if (env.getEnvName().equals("prod")) {
                if (e.getName().startsWith("gray:")) {
                    return true;
                }
                return e.getName().startsWith("canary:");
            }
            return false;
        }).collect(Collectors.toList());
        return BeanCopierUtil.copyListProperties(result, ApplicationResourceVO.BaseResource.class);
    }

}
