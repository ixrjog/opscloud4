package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.facade.leo.LeoBuildFacade;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/11/8 17:33
 * @Version 1.0
 */
class LeoBuildFacadeImplTest extends BaseUnit {

    @Resource
    private LeoBuildFacade leoBuildFacade;

    @Test
    void doBuildTest(){
        LeoBuildParam.DoBuild doBuild = LeoBuildParam.DoBuild.builder()
                .jobId(1)
                .build();
        leoBuildFacade.doBuild(doBuild );
    }

}