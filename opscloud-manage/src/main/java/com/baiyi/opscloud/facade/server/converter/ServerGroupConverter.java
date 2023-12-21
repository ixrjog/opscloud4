package com.baiyi.opscloud.facade.server.converter;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.ValidationUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;

/**
 * @Author baiyi
 * @Date 2023/12/15 11:45
 * @Version 1.0
 */
public class ServerGroupConverter {

    public static ServerGroup to(ServerGroupParam.AddServerGroup addServerGroup) {
        ServerGroup serverGroup = BeanCopierUtil.copyProperties(addServerGroup, ServerGroup.class);
        return to(serverGroup);
    }

    public static ServerGroup to(ServerGroupParam.UpdateServerGroup updateServerGroup) {
        ServerGroup serverGroup = BeanCopierUtil.copyProperties(updateServerGroup, ServerGroup.class);
        return to(serverGroup);
    }

    private static ServerGroup to(ServerGroup serverGroup) {
        serverGroup.setName(serverGroup.getName().trim());
        ValidationUtil.tryServerGroupNameRule(serverGroup.getName());
        return serverGroup;
    }

}