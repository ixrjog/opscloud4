package com.baiyi.opscloud.leo;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import com.baiyi.opscloud.leo.handler.deploy.chain.post.fork.ForkToStable;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/25 14:14
 * &#064;Version 1.0
 */
public class ForkToStableTest extends BaseUnit {

    @Resource
    private ForkToStable forkToStable;

    @Resource
    private LeoDeployService leoDeployService;

    @Test
    void test() {
        LeoDeploy leoDeploy = leoDeployService.getById(160840);
        LeoDeployModel.DeployConfig config = LeoDeployModel.load(leoDeploy);
        boolean b = forkToStable.preValidation(leoDeploy, config);
        System.out.println(b);
    }

}
