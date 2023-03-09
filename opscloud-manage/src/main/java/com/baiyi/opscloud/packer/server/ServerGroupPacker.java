package com.baiyi.opscloud.packer.server;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.packer.ServerGroupPackerDelegate;
import com.baiyi.opscloud.packer.business.BusinessPropertyPacker;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/24 1:36 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public final class ServerGroupPacker implements IWrapper<ServerGroupVO.ServerGroup> {

    private final ServerGroupTypePacker serverGroupTypePacker;

    private final ServerService serverService;

    private final ServerGroupService serverGroupService;

    private final BusinessPropertyPacker businessPropertyPacker;

    private final ServerGroupPackerDelegate serverGroupPackerDelegate;

    @Override
    public void wrap(ServerGroupVO.ServerGroup serverGroup, IExtend iExtend) {
        // 代理
        serverGroupPackerDelegate.wrap(serverGroup, SimpleExtend.EXTEND);
        serverGroupTypePacker.wrap(serverGroup);
        businessPropertyPacker.wrap(serverGroup);
        serverGroup.setServerSize(serverService.countByServerGroupId(serverGroup.getId()));
    }

    public void wrap(ServerGroupVO.IServerGroup iServerGroup) {
        ServerGroup serverGroup = serverGroupService.getById(iServerGroup.getServerGroupId());
        iServerGroup.setServerGroup(BeanCopierUtil.copyProperties(serverGroup, ServerGroupVO.ServerGroup.class));
    }

}
