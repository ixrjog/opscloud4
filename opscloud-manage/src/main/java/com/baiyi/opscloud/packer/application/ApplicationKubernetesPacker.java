package com.baiyi.opscloud.packer.application;

import com.baiyi.opscloud.common.annotation.BizDocWrapper;
import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.sys.EnvService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/3/27 17:09
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApplicationKubernetesPacker {

    private final ApplicationResourceService applicationResourceService;

    private final ApplicationResourcePacker resourcePacker;

    private final EnvService envService;

    private final ApplicationArmsPacker applicationArmsPacker;

    @TagsWrapper(extend = true)
    @BizDocWrapper(extend = true)
    public void wrap(ApplicationVO.Kubernetes kubernetes, int envType) {
        Env env = envService.getByEnvType(envType);
        List<ApplicationResource> resources = getResources(kubernetes.getId(), env);

        List<ApplicationResourceVO.Resource> data = BeanCopierUtil.copyListProperties(resources, ApplicationResourceVO.Resource.class)
                .stream()
                .peek(resourcePacker::wrap)
                .collect(Collectors.toList());
        kubernetes.setResources(data);
        // ARMS
        applicationArmsPacker.warp(env,kubernetes);
    }

    private List<ApplicationResource> getResources(int applicationId, Env env) {
        return applicationResourceService.queryByApplication(applicationId, DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name())
                .stream().filter(e -> {
                    if (e.getName().startsWith(env.getEnvName() + ":")) {
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
                        return e.getName().startsWith("canary:");
                    }
                    return false;
                }).collect(Collectors.toList());
    }

}