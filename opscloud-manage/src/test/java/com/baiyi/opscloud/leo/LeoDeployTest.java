package com.baiyi.opscloud.leo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2023/3/29 11:44
 * @Version 1.0
 */
public class LeoDeployTest extends BaseUnit {

    @Resource
    private LeoDeployService deployService;

    @Test
    void test() {
        LeoDeploy leoDeploy = deployService.getById(1);
        LeoDeployModel.DeployConfig config = LeoDeployModel.load(leoDeploy.getDeployConfig());
        print(config);
    }

}
