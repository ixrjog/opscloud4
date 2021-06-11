package com.baiyi.caesar.packer.server;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.ServerGroup;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.service.server.ServerService;
import com.baiyi.caesar.util.ExtendUtil;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
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

    public ServerGroupVO.ServerGroup wrap(ServerGroup serverGroup){
        return BeanCopierUtil.copyProperties(serverGroup, ServerGroupVO.ServerGroup.class);
    }

    private void wrap(ServerGroupVO.ServerGroup vo) {
        serverGroupTypePacker.wrap(vo);
        vo.setServerSize(serverService.countByServerGroupId(vo.getId()));
    }

}
