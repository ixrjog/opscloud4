package com.baiyi.opscloud.opscloud;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.server.ServerAccountParam;
import com.baiyi.opscloud.facade.server.ServerAccountFacade;
import com.baiyi.opscloud.opscloud.provider.OcServerProvider;
import com.baiyi.opscloud.opscloud.vo.OcServerVO;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author baiyi
 * @Date 2021/6/24 10:39 上午
 * @Version 1.0
 */
@Slf4j
public class OcServerTest extends BaseUnit {

    @Resource
    private OcServerProvider ocServerProvider;

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private ServerAccountFacade serverAccountFacade;

    @Resource
    private ServerService serverService;

    @Test
    void syncServers() {
        try {
            DataTable<OcServerVO.Server> table = ocServerProvider.queryServers();
            for (OcServerVO.Server s : table.getData()) {
                if (serverService.getByPrivateIp(s.getPrivateIp()) != null) continue;
                log.info(JSON.toJSONString(s));
                ServerGroup serverGroup = serverGroupService.getByName(s.getServerGroup().getName());
                Server server = BeanCopierUtil.copyProperties(s, Server.class);
                server.setId(null);
                server.setOsType("Linux");
                server.setServerGroupId(serverGroup.getId()); // 重置服务器组id
                server.setServerType(0); // 虚拟服务器
                serverService.add(server);
                saveServerAccount(server.getId());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveServerAccount(int serverId) {
        ServerAccountParam.UpdateServerAccountPermission updatePermission = ServerAccountParam.UpdateServerAccountPermission.builder()
                .serverId(serverId)
                .accountIds(Sets.newHashSet(Lists.newArrayList(2, 3)))
                .build();
        serverAccountFacade.updateServerAccountPermission(updatePermission);
    }


}