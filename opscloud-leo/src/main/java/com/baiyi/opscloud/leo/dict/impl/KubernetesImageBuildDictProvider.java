package com.baiyi.opscloud.leo.dict.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.leo.handler.build.strategy.verification.PostBuildVerificationWithKubernetesImageStrategy.KUBERNETES_IMAGE;

/**
 * @Author baiyi
 * @Date 2023/4/25 11:43
 * @Version 1.0
 */
@Slf4j
@Component
public class KubernetesImageBuildDictProvider extends BaseBuildDictProvider {

    @Override
    public String getBuildType() {
        return KUBERNETES_IMAGE;
    }

}
