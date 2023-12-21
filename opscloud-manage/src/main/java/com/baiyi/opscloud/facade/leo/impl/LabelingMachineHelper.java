package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.baiyi.opscloud.service.tag.SimpleTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.baiyi.opscloud.common.base.Global.AUTO_BUILD;
import static com.baiyi.opscloud.common.base.Global.AUTO_DEPLOY;

/**
 * @Author baiyi
 * @Date 2023/5/9 14:20
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LabelingMachineHelper {

    private final SimpleTagService simpleTagService;

    /**
     * 打标签
     * @param o
     * @param businessType
     * @param businessId
     */
    public void labeling(Object o, Integer businessType, Integer businessId) {
        if (o instanceof LeoBuildParam.IAutoBuild autoBuild) {
            if (Optional.ofNullable(autoBuild.getAutoBuild()).orElse(false)) {
                simpleTagService.labeling(AUTO_BUILD, businessType, businessId);
            }
        }
        if (o instanceof LeoDeployParam.IAutoDeploy autoDeploy) {
            if (Optional.ofNullable(autoDeploy.getAutoDeploy()).orElse(false)) {
                simpleTagService.labeling(AUTO_DEPLOY, businessType, businessId);
            }
        }
    }

}