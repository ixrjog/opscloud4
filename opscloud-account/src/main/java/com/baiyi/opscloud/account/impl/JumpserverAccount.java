package com.baiyi.opscloud.account.impl;

import com.baiyi.opscloud.account.IAccount;
import com.baiyi.opscloud.account.base.AccountType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUserGroups;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUsergroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserCredential;
import com.baiyi.opscloud.domain.vo.user.OcUserCredentialVO;
import com.baiyi.opscloud.jumpserver.api.JumpserverAPI;
import com.baiyi.opscloud.jumpserver.builder.UsersUserBuilder;
import com.baiyi.opscloud.jumpserver.center.JumpserverCenter;
import com.baiyi.opscloud.jumpserver.util.JumpserverUtils;
import com.baiyi.opscloud.service.jumpserver.UsersUserGroupsService;
import com.baiyi.opscloud.service.jumpserver.UsersUserService;
import com.baiyi.opscloud.service.jumpserver.UsersUsergroupService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/13 11:57 下午
 * @Version 1.0
 */
@Component("JumpserverAccount")
public class JumpserverAccount extends BaseAccount implements IAccount {

    @Resource
    private JumpserverCenter jumpserverCenter;

    @Resource
    private UsersUserService usersUserService;

    @Resource
    private UsersUsergroupService usersUsergroupService;

    @Resource
    private UsersUserGroupsService usersUserGroupsService;

    @Resource
    private JumpserverAPI jumpserverAPI;

    @Override
    protected List<OcUser> getUserList() {
        return null;
    }

    @Override
    protected List<OcAccount> getOcAccountList() {
        return null;
    }

    @Override
    protected int getAccountType() {
        return AccountType.JUMPSEVER.getType();
    }


    @Override
    public Boolean sync() {
        List<OcUser> userList = ocUserService.queryOcUserActive();
        for (OcUser ocUser : userList)
            syncUsersUser(ocUser);
        return true;
    }

    @Override
    public Boolean sync(OcUser user) {
        syncUsersUser(user);
        return true;
    }

    private void syncUsersUser(OcUser ocUser) {
        // 只同步有服务器组授权的用户
        List<OcServerGroup> serverGroupList = ocServerGroupService.queryUserPermissionOcServerGroupByUserId(ocUser.getId());
        if (serverGroupList.isEmpty()) return;
        UsersUser usersUser = jumpserverCenter.saveUsersUser(ocUser);
        bindUserGroups(usersUser, serverGroupList);
    }

    /**
     * 绑定用户-用户组
     *
     * @param usersUser
     * @param serverGroupList
     */
    private void bindUserGroups(UsersUser usersUser, List<OcServerGroup> serverGroupList) {
        for (OcServerGroup ocServerGroup : serverGroupList)
            jumpserverCenter.bindUserGroups(usersUser, ocServerGroup);
    }


    /**
     * 创建
     *
     * @return
     */
    @Override
    public Boolean create(OcUser user) {
        return update(user);
    }

    /**
     * 移除
     *
     * @return
     */
    @Override
    public Boolean delete(OcUser user) {
        return jumpserverCenter.delUsersUser(user.getUsername());
    }

    @Override
    public Boolean update(OcUser user) {
        UsersUser usersUser = jumpserverCenter.saveUsersUser(user);
        if (usersUser == null)
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    @Override
    public Boolean grant(OcUser ocUser, String resource) {
        UsersUser usersUser = createUsersUser(ocUser);
        //UsersUser usersUser = usersUserService.queryUsersUserByUsername(ocUser.getUsername());
        if (usersUser == null) return Boolean.FALSE;
        String name = JumpserverUtils.toUsergroupName(resource);
        UsersUsergroup usersUsergroup = usersUsergroupService.queryUsersUsergroupByName(name);
        if (usersUsergroup == null) return Boolean.FALSE;
        UsersUserGroups pre = new UsersUserGroups();
        pre.setUsergroupId(usersUsergroup.getId());
        pre.setUserId(usersUser.getId());
        UsersUserGroups usersUserGroups = usersUserGroupsService.queryUsersUserGroupsByUniqueKey(pre);
        if (usersUserGroups == null)
            usersUserGroupsService.addUsersUserGroups(pre);
        return Boolean.TRUE;
    }

    @Override
    public Boolean revoke(OcUser ocUser, String resource) {
        UsersUser usersUser = usersUserService.queryUsersUserByUsername(ocUser.getUsername());
        if (usersUser == null) return Boolean.TRUE;
        String name = JumpserverUtils.toUsergroupName(resource);
        UsersUsergroup usersUsergroup = usersUsergroupService.queryUsersUsergroupByName(name);
        if (usersUsergroup == null) return Boolean.TRUE;
        UsersUserGroups pre = new UsersUserGroups();
        pre.setUsergroupId(usersUsergroup.getId());
        pre.setUserId(usersUser.getId());
        UsersUserGroups usersUserGroups = usersUserGroupsService.queryUsersUserGroupsByUniqueKey(pre);
        if (usersUserGroups != null)
            usersUserGroupsService.delUsersUserGroupsById(usersUserGroups.getId());
        return Boolean.TRUE;
    }

    @Override
    public Boolean active(OcUser user, boolean active) {
        return jumpserverCenter.activeUsersUser(user.getUsername(), active);
    }

    /**
     * 推送用户公钥 PubKey
     *
     * @param ocUser
     * @return
     */
    @Override
    public Boolean pushSSHKey(OcUser ocUser) {
        OcUserCredential credential = getOcUserSSHPubKey(ocUser);
        if (credential == null) return Boolean.FALSE;
        //return jumpserverCenter.pushKey(user,BeanCopierUtils.copyProperties(credential,OcUserCredentialVO.UserCredential.class));

        UsersUser usersUser = saveUsersUser(ocUser);
        if (usersUser == null)
            return false;
        return jumpserverAPI.pushKey(ocUser, usersUser, BeanCopierUtils.copyProperties(credential, OcUserCredentialVO.UserCredential.class));

    }

    private UsersUser createUsersUser(OcUser ocUser) {
        if (StringUtils.isEmpty(ocUser.getEmail()))
            return null;
        UsersUser usersUser = usersUserService.queryUsersUserByUsername(ocUser.getUsername());
        if (usersUser != null)
            return usersUser;
        return saveUsersUser(ocUser);
    }

    private UsersUser saveUsersUser(OcUser ocUser) {
        if (StringUtils.isEmpty(ocUser.getEmail()))
            return null;
        UsersUser checkUsersUser = usersUserService.queryUsersUserByUsername(ocUser.getUsername());
        UsersUser checkUserEmail = usersUserService.queryUsersUserByEmail(ocUser.getEmail());
        UsersUser usersUser = null;
        if (checkUsersUser == null) {
            if (checkUserEmail == null) {
                usersUser = UsersUserBuilder.build(ocUser);
                usersUserService.addUsersUser(usersUser);
            }
        } else {
            if (checkUsersUser.getEmail().equals(ocUser.getEmail())) {
                usersUser = checkUsersUser;
                usersUser.setName(ocUser.getDisplayName());
                usersUser.setPhone(ocUser.getPhone() == null ? "" : ocUser.getPhone());
                usersUser.setWechat(ocUser.getWechat() == null ? "" : ocUser.getWechat());
                usersUserService.updateUsersUser(usersUser);
            }
        }
        return usersUser;
    }
}
