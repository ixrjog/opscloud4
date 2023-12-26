package com.baiyi.opscloud.leo.dict.impl;

import com.baiyi.opscloud.leo.constants.BuildTypeConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/4/25 11:43
 * @Version 1.0
 */
@Slf4j
@Component
public class KubernetesImageBuildDictGenerator extends BaseBuildDictGenerator {

    @Override
    public String getBuildType() {
        return BuildTypeConstants.KUBERNETES_IMAGE;
    }

}