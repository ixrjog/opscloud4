package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.service.tag.SimpleTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.common.base.Global.AUTO_DEPLOY;

/**
 * @Author baiyi
 * @Date 2023/5/9 14:20
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AutoDeployHelper {

    private final SimpleTagService simpleTagService;

    /**
     * 打AutoDeploy标签
     *
     * @param iAutoDeploy
     * @param businessId
     */
    public void labeling(LeoBuildParam.IAutoDeploy iAutoDeploy, Integer businessType, Integer businessId) {
        if (!iAutoDeploy.getAutoDeploy()) {
            return;
        }
        simpleTagService.labeling(AUTO_DEPLOY, businessType, businessId);
    }

}
