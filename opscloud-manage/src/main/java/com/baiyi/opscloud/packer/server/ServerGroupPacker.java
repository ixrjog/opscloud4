package com.baiyi.opscloud.packer.server;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.util.ExtendUtil;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/24 1:36 下午
 * @Version 1.0
 */
@Component
public final class ServerGroupPacker {

    @Resource
    private ServerGroupTypePacker serverGroupTypePacker;

    @Resource
    private ServerService serverService;

    @Resource
    private ServerGroupService serverGroupService;

    public List<ServerGroupVO.ServerGroup> wrapVOList(List<ServerGroup> data) {
        return BeanCopierUtil.copyListProperties(data, ServerGroupVO.ServerGroup.class);
    }

    public List<ServerGroupVO.ServerGroup> wrapVOList(List<ServerGroup> data, IExtend iExtend) {
        List<ServerGroupVO.ServerGroup> voList = wrapVOList(data);
        if (!ExtendUtil.isExtend(iExtend))
            return voList;
        return voList.stream().peek(this::wrap).collect(Collectors.toList());
    }

    public List<ServerGroupVO.ServerGroup> wrapVOList(List<ServerGroup> data, UserVO.IUserPermission iUserPermission) {
        List<ServerGroupVO.ServerGroup> voList = wrapVOList(data);
        return voList.stream().peek(this::wrap).collect(Collectors.toList());
    }

    public ServerGroupVO.ServerGroup wrap(ServerGroup serverGroup) {
        return BeanCopierUtil.copyProperties(serverGroup, ServerGroupVO.ServerGroup.class);
    }

    private void wrap(ServerGroupVO.ServerGroup vo) {
        serverGroupTypePacker.wrap(vo);
        vo.setServerSize(serverService.countByServerGroupId(vo.getId()));
    }

    public void wrap(ServerGroupVO.IServerGroup iServerGroup) {
        ServerGroup serverGroup = serverGroupService.getById(iServerGroup.getServerGroupId());
        iServerGroup.setServerGroup(wrap(serverGroup));
    }

}
