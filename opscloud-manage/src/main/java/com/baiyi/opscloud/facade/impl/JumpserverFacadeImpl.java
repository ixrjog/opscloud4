package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.facade.JumpserverFacade;
import com.baiyi.opscloud.jumpserver.center.JumpserverCenter;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.user.OcUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/8 4:18 下午
 * @Version 1.0
 */
@Service
public class JumpserverFacadeImpl implements JumpserverFacade {

    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private JumpserverCenter jumpserverCenter;

    // 授权规则前缀
    public final String PERMS_PREFIX = "perms_";

    // 过期时间
    public final String DATE_EXPIRED = "2089-01-01 00:00:00";

    // 管理员用户组 绑定 根节点
    public final String USERGROUP_ADMINISTRATORS = "usergroup_administrators";
    // 管理员授权
    public final String PERMS_ADMINISTRATORS = "perms_administrators";

    public BusinessWrapper<Boolean> syncUsers() {
        List<OcUser> userList = ocUserService.queryOcUserActive();
        for (OcUser ocUser : userList)
            bindUserGroups(ocUser );
        return new BusinessWrapper<Boolean>(true);
    }

    /**
     * 绑定用户-用户组
     *
     * @param ocUser
     * @return
     */
    private void bindUserGroups(OcUser ocUser) {
        // 查询用户的所有授权的服务组
        List<OcServerGroup> serverGroupList =  ocServerGroupService.queryUerPermissionOcServerGroupByUserId(ocUser.getId());
        // 创建用户
        UsersUser usersUser = jumpserverCenter.createUsersUser(ocUser);
        for (OcServerGroup ocServerGroup : serverGroupList)
            jumpserverCenter.bindUserGroups(usersUser, ocServerGroup);
    }



}
