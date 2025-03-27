package com.baiyi.opscloud.facade.leo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoDeploymentVersionDetailsRequestParam;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVersionVO;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
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

    @Test
    void test1() {
        SubscribeLeoDeploymentVersionDetailsRequestParam queryParam = new SubscribeLeoDeploymentVersionDetailsRequestParam();
        queryParam.setApplicationId(460);
        queryParam.setEnvType(4);
        queryParam.setPage(1);
        queryParam.setLength(3);
        List<LeoJobVersionVO.JobVersion> jobVersionList = leoDeployFacade.queryMyLeoJobVersion(queryParam);
        System.out.println(jobVersionList);
    }

}