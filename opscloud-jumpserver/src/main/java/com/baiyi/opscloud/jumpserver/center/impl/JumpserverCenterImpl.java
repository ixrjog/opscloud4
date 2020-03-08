package com.baiyi.opscloud.jumpserver.center.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUserGroups;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUsergroup;
import com.baiyi.opscloud.jumpserver.bo.UsersUsergroupBO;
import com.baiyi.opscloud.jumpserver.builder.UsersUserBuilder;
import com.baiyi.opscloud.jumpserver.center.JumpserverCenter;
import com.baiyi.opscloud.jumpserver.config.JumpserverConfig;
import com.baiyi.opscloud.jumpserver.util.JumpserverUtils;
import com.baiyi.opscloud.service.jumpserver.UsersUserGroupsService;
import com.baiyi.opscloud.service.jumpserver.UsersUserService;
import com.baiyi.opscloud.service.jumpserver.UsersUsergroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/9 11:39 上午
 * @Version 1.0
 */
@Slf4j
@Component("JumpserverUserCenter")
public class JumpserverCenterImpl implements JumpserverCenter {

    @Resource
    private JumpserverConfig jumpserverConfig;

    @Resource
    private UsersUserService usersUserService;

    @Resource
    private UsersUsergroupService usersUsergroupService;

    @Resource
    private UsersUserGroupsService usersUserGroupsService;

    public static final String DATE_EXPIRED = "2089-01-01 00:00:00";

    @Override
    public void bindUserGroups(UsersUser usersUser, OcServerGroup ocServerGroup) {
        // 用户组名称
        String usergroupName = JumpserverUtils.toUsergroupName(ocServerGroup.getName());
        UsersUsergroup usersUsergroup = usersUsergroupService.queryUsersUsergroupByName(usergroupName);
        if (usersUsergroup == null)
            usersUsergroup = createUsersUsergroup(ocServerGroup);
        bindUserGroups(usersUser, usersUsergroup);
    }

    private void bindUserGroups(UsersUser usersUser, UsersUsergroup usersUsergroup) {
        // UsersUserGroups usersUserGroups = new UsersUserGroupsDO(usersUserDO.getId(), usersUsergroupDO.getId());
        UsersUserGroups pre = new UsersUserGroups();
        pre.setUserId(usersUser.getId());
        pre.setUsergroupId(usersUsergroup.getId());
        UsersUserGroups usersUserGroups = usersUserGroupsService.queryUsersUserGroupsByUniqueKey(pre);
        if (usersUserGroups == null)
            usersUserGroupsService.addUsersUserGroups(usersUserGroups);
    }

    private UsersUsergroup createUsersUsergroup(OcServerGroup ocServerGroup) {
        String usergroupName = JumpserverUtils.toUsergroupName(ocServerGroup.getName());
        return createUsersUsergroup(usergroupName, ocServerGroup.getComment());
    }

    private UsersUsergroup createUsersUsergroup(String name, String comment) {
        UsersUsergroupBO usersUsergroupBO = UsersUsergroupBO.builder()
                .id(UUIDUtils.getUUID())
                .name(name)
                .comment(comment)
                .build();
        UsersUsergroup usersUsergroup = BeanCopierUtils.copyProperties(usersUsergroupBO, UsersUsergroup.class);
        try {
            usersUsergroupService.addUsersUsergroup(usersUsergroup);
        } catch (Exception e) {
            log.error("Jumpserver addUsersUsergroup Error {} !", name);
        }
        return usersUsergroup;
    }

    /**
     * 创建Jumpserver用户
     *
     * @param ocUser
     * @return
     */
    @Override
    public UsersUser createUsersUser(OcUser ocUser) {
        UsersUser usersUser = usersUserService.queryUsersUserByUsername(ocUser.getUsername());
        if (usersUser == null) {
            usersUser = UsersUserBuilder.build(ocUser);
            usersUserService.addUsersUser(usersUser);
        }
        return usersUser;
    }


}
