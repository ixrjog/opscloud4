package com.baiyi.caesar.sshserver.packer;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.Env;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.generator.caesar.ServerGroup;
import com.baiyi.caesar.domain.generator.caesar.UserPermission;
import com.baiyi.caesar.domain.vo.env.EnvVO;
import com.baiyi.caesar.domain.vo.server.ServerGroupVO;
import com.baiyi.caesar.domain.vo.server.ServerVO;
import com.baiyi.caesar.domain.vo.user.UserPermissionVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import com.baiyi.caesar.service.server.ServerGroupService;
import com.baiyi.caesar.service.sys.EnvService;
import com.baiyi.caesar.service.user.UserPermissionService;
import com.baiyi.caesar.util.ServerUtil;
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
        return servers.stream().map(s -> {
            ServerVO.Server server = BeanCopierUtil.copyProperties(s, ServerVO.Server.class);
            ServerGroup group = serverGroupService.getById(server.getServerGroupId());
            ServerGroupVO.ServerGroup serverGroup = BeanCopierUtil.copyProperties(group, ServerGroupVO.ServerGroup.class);
            wrap(serverGroup);
            server.setServerGroup(serverGroup);
            Env env = envService.getByEnvType(server.getEnvType());
            server.setEnv(BeanCopierUtil.copyProperties(env, EnvVO.Env.class));
            ServerUtil.wrapDisplayName(server);
            return server;
        }).collect(Collectors.toList());


    }
}
