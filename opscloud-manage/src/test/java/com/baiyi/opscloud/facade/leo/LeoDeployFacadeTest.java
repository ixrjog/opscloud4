package com.baiyi.opscloud.facade.leo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2023/9/26 10:37
 * @Version 1.0
 */
class LeoDeployFacadeTest extends BaseUnit {

    @Resource
    private LeoDeployFacade leoDeployFacade;


    /**
     * {"assetId":281339,"templateLabels":{"msePilotCreateAppName":"qa-basic-service-frankfurt-daily","msePilotAutoEnable":"on"}}
     */
    @Test
    void updateTest() {
        Map<String, String> templateLabels = Maps.newHashMap();
        templateLabels.put("msePilotCreateAppName", "qa-basic-service-frankfurt-daily");
        templateLabels.put("msePilotAutoEnable", "on");

        LeoDeployParam.UpdateDeployDeployment updateDeployDeployment = LeoDeployParam.UpdateDeployDeployment.builder()
                .assetId(281339)
                .templateLabels(templateLabels)
                .build();
        leoDeployFacade.updateDeployDeployment(updateDeployDeployment);
    }

}