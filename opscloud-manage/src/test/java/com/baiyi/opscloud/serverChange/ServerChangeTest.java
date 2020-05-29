package com.baiyi.opscloud.serverChange;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.base.ServerChangeType;
import com.baiyi.opscloud.domain.param.server.ServerChangeParam;
import com.baiyi.opscloud.facade.ServerChangeFacade;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/29 1:29 下午
 * @Version 1.0
 */
public class ServerChangeTest extends BaseUnit {

    @Resource
    private ServerChangeFacade serverChangeFacade;

    @Test
    void testAttributeGroups() {
        ServerChangeParam.ExecuteServerChangeParam param = new ServerChangeParam.ExecuteServerChangeParam();
        param.setChangeType(ServerChangeType.OFFLINE.getType());
        param.setServerGroupId(492);
        param.setServerId(4298);

        serverChangeFacade.executeServerChangeOffline(param);
    }
}
