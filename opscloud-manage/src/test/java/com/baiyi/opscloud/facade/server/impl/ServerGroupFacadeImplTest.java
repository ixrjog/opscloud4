package com.baiyi.opscloud.facade.server.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import com.baiyi.opscloud.facade.server.ServerGroupFacade;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/25 9:15 上午
 * @Version 1.0
 */
class ServerGroupFacadeImplTest extends BaseUnit {

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @Test
    void deleteServerGroupByIdTest() {
      //  serverGroupFacade.deleteServerGroupById(1);
    }

    @Test
    void queryServerGroupHostPatternByEnvTest(){
        ServerGroupParam.ServerGroupEnvHostPatternQuery query = ServerGroupParam.ServerGroupEnvHostPatternQuery.builder()
                .serverGroupName("group_opscloud4")
                .envType(4)
                .build();
        Map<String, List<Server>> map= serverGroupFacade.queryServerGroupHostPatternByEnv(query);
        print(map);
    }

}