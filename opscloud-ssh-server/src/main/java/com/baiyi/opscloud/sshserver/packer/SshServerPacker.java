package com.baiyi.opscloud.sshserver.packer;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.generator.opscloud.ServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.domain.vo.user.UserPermissionVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.server.SimpleServerNameFacade;
import com.baiyi.opscloud.packer.ServerPackerDelegate;
import com.baiyi.opscloud.service.server.ServerGroupService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/6/11 1:49 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SshServerPacker {

    private final ServerGroupService serverGroupService;

    private final UserPermissionService permissionService;

    private final ServerPackerDelegate serverPackerDelegate;

    private void wrap(UserVO.IUserPermission iUserPermission) {
        UserPermission query = UserPermission.builder()
                .userId(iUserPermission.getUserId())
                .businessId(iUserPermission.getBusinessId())
                .businessType(iUserPermission.getBusinessType())
                .build();

        UserPermission userPermission = permissionService.getByUserPermission(query);
        if (userPermission != null) {
            iUserPermission.setUserPermission(BeanCopierUtil.copyProperties(userPermission, UserPermissionVO.UserPermission.class));
        }
    }

    public void wrap(ServerVO.Server server) {
        serverPackerDelegate.wrap(server, SimpleExtend.EXTEND);
        ServerGroup group = serverGroupService.getById(server.getServerGroupId());
        ServerGroupVO.ServerGroup serverGroup = BeanCopierUtil.copyProperties(group, ServerGroupVO.ServerGroup.class);
        serverGroup.setUserId(SessionHolder.getUserId());
        wrap(serverGroup);
        server.setServerGroup(serverGroup);
        SimpleServerNameFacade.wrapDisplayName(server);
    }

}