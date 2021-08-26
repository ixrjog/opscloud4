package com.baiyi.opscloud.sshserver.packer;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.domain.vo.user.UserPermissionVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.packer.tag.TagPacker;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/11 1:49 下午
 * @Version 1.0
 */
@Component
public class SshServerPacker {

    @Resource
    private ServerGroupService serverGroupService;

    @Resource
    private EnvService envService;

    @Resource
    private UserPermissionService permissionService;

    @Resource
    private TagPacker tagPacker;

    public void wrap(UserVO.IUserPermission iUserPermission) {
        UserPermission query = UserPermission.builder()
                .userId(iUserPermission.getUserId())
                .businessId(iUserPermission.getBusinessId())
                .businessType(iUserPermission.getBusinessType())
                .build();

        UserPermission userPermission = permissionService.getByUserPermission(query);
        if (userPermission != null)
            iUserPermission.setUserPermission(BeanCopierUtil.copyProperties(userPermission, UserPermissionVO.UserPermission.class));
    }

    public List<ServerVO.Server> wrapToVO(List<Server> servers) {
        return servers.stream().map(this::wrapToVO
        ).collect(Collectors.toList());
    }

    public ServerVO.Server wrapToVO(Server server){
        ServerVO.Server vo = BeanCopierUtil.copyProperties(server, ServerVO.Server.class);
        ServerGroup group = serverGroupService.getById(server.getServerGroupId());
        ServerGroupVO.ServerGroup serverGroup = BeanCopierUtil.copyProperties(group, ServerGroupVO.ServerGroup.class);
        serverGroup.setUserId(SessionUtil.getUserId());
        wrap(serverGroup);
        vo.setServerGroup(serverGroup);
        Env env = envService.getByEnvType(vo.getEnvType());
        vo.setEnv(BeanCopierUtil.copyProperties(env, EnvVO.Env.class));
        SimpleServerNameFacade.wrapDisplayName(vo);
        tagPacker.wrap(vo);
        return vo;
    }
}
