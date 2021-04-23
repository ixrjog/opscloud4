package com.baiyi.opscloud.jumpserver.center.impl;

import com.baiyi.opscloud.common.base.SettingName;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.jumpserver.*;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.facade.SettingBaseFacade;
import com.baiyi.opscloud.jumpserver.bo.AssetsSystemuserAssetsBO;
import com.baiyi.opscloud.jumpserver.bo.UsersUsergroupBO;
import com.baiyi.opscloud.jumpserver.builder.AssetsNodeBuilder;
import com.baiyi.opscloud.jumpserver.builder.PermsAssetpermissionBuilder;
import com.baiyi.opscloud.jumpserver.builder.UsersUserBuilder;
import com.baiyi.opscloud.jumpserver.center.JumpserverCenter;
import com.baiyi.opscloud.jumpserver.util.JumpserverUtils;
import com.baiyi.opscloud.service.jumpserver.*;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/9 11:39 上午
 * @Version 1.0
 */
@Slf4j
@Component("JumpserverCenter")
public class JumpserverCenterImpl implements JumpserverCenter {

    @Resource
    private UsersUserService usersUserService;

    @Resource
    private UsersUsergroupService usersUsergroupService;

    @Resource
    private UsersUserGroupsService usersUserGroupsService;

    @Resource
    private AssetsNodeService assetsNodeService;

    @Resource
    private PermsAssetpermissionService permsAssetpermissionService;

    @Resource
    private PermsAssetpermissionSystemUsersService permsAssetpermissionSystemUsersService;

    @Resource
    private PermsAssetpermissionUserGroupsServcie permsAssetpermissionUserGroupsServcie;

    @Resource
    private PermsAssetpermissionNodesService permsAssetpermissionNodesService;

    @Resource
    private AssetsAssetService assetsAssetService;

    @Resource
    private AssetsAssetNodesService assetsAssetNodesService;

    @Resource
    private AssetsSystemuserAssetsService assetsSystemuserAssetsService;

    @Resource
    private OpsAdhocHostsService opsAdhocHostsService;

    @Resource
    private SettingBaseFacade settingBaseFacade;

//    @Resource
//    private JumpserverAPI jumpserverAPI;
//
//    @Resource
//    private JumpserverAssetAPI jumpserverAssetAPI;

    public static final String DATE_START = "2020-10-01 00:00:00";

    public static final String DATE_EXPIRED = "2089-01-01 00:00:00";
    // 管理员用户组 绑定 根节点
    public final String USERGROUP_ADMINISTRATORS = "usergroup_administrators";
    // 管理员授权
    public final String PERMS_ADMINISTRATORS = "perms_administrators";

    @Override
    public void bindUserGroups(UsersUser usersUser, OcServerGroup ocServerGroup) {
        // 用户组名称
        String usergroupName = JumpserverUtils.toUsergroupName(ocServerGroup.getName());
        UsersUsergroup usersUsergroup = usersUsergroupService.queryUsersUsergroupByName(usergroupName);
        if (usersUsergroup == null)
            usersUsergroup = saveUsersUsergroup(ocServerGroup);
        bindUserGroups(usersUser, usersUsergroup);
    }

    private void bindUserGroups(UsersUser usersUser, UsersUsergroup usersUsergroup) {
        UsersUserGroups pre = new UsersUserGroups();
        pre.setUserId(usersUser.getId());
        pre.setUsergroupId(usersUsergroup.getId());
        UsersUserGroups usersUserGroups = usersUserGroupsService.queryUsersUserGroupsByUniqueKey(pre);
        if (usersUserGroups == null)
            usersUserGroupsService.addUsersUserGroups(pre);
    }

    @Override
    public UsersUsergroup saveUsersUsergroup(OcServerGroup ocServerGroup) {
        String usergroupName = JumpserverUtils.toUsergroupName(ocServerGroup.getName());
        UsersUsergroup check = usersUsergroupService.queryUsersUsergroupByName(usergroupName);
        if (check != null)
            return check;
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
            e.printStackTrace();
            log.error("Jumpserver addUsersUsergroup Error {} !", name);
            return null;
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
    public UsersUser saveUsersUser(OcUser ocUser) {
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
                usersUser = UsersUserBuilder.build(ocUser, checkUsersUser.getRole());
                usersUser.setId(checkUsersUser.getId());
                if (!StringUtils.isEmpty(checkUsersUser.getPublicKey())) // 写入publicKey
                    usersUser.setPublicKey(checkUsersUser.getPublicKey());
                usersUserService.updateUsersUser(usersUser);
            }
        }
        return usersUser;
    }

    /**
     * 2019.4.17 修改计算节点最大值算法
     * 创建节点（服务器组）
     *
     * @param ocServerGroup
     * @return
     */
    @Override
    public AssetsNode createAssetsNode(OcServerGroup ocServerGroup) {
        AssetsNode assetsNode = assetsNodeService.queryAssetsNodeByValue(ocServerGroup.getName());
        if (assetsNode == null) {
            assetsNode = AssetsNodeBuilder.build(ocServerGroup, getAssetsNodeKey());
            assetsNodeService.addAssetsNode(assetsNode);
        }
        return assetsNode;
    }

    /**
     * 取节点Key(按最后数据逐条查询)
     *
     * @return
     */
    private String getAssetsNodeKey() {
        AssetsNode lastNode = assetsNodeService.queryAssetsNodeLastOne();
        try {
            String[] keys = lastNode.getKey().split(":");
            if (keys.length == 1)
                return "1:1";
            int k = Integer.parseInt(keys[1]);
            String keyName;
            while (true) {
                k++;
                keyName = Joiner.on(":").join("1", k);
                AssetsNode checkAN = assetsNodeService.queryAssetsNodeByKey(keyName);
                if (checkAN == null) return keyName;
            }
        } catch (Exception e) {
            return "1:1";
        }

    }

    @Override
    public PermsAssetpermission createPermsAssetpermission(OcServerGroup ocServerGroup, AssetsNode assetsNode, UsersUsergroup usersUsergroup) {
        String name = JumpserverUtils.toPermsAssetpermissionName(ocServerGroup.getName());
        PermsAssetpermission permsAssetpermission = permsAssetpermissionService.queryPermsAssetpermissionByName(name);
        if (permsAssetpermission == null) {
            // 新增授权策略
            permsAssetpermission = PermsAssetpermissionBuilder.build(name);
            permsAssetpermissionService.addPermsAssetpermission(permsAssetpermission);
        }
        // 绑定系统账户
        bindPermsAssetpermissionSystemUsers(permsAssetpermission);
        // 绑定用户组
        bindPermsAssetpermissionUserGroups(permsAssetpermission, usersUsergroup);
        // 绑定节点
        bindPermsAssetpermissionNodes(permsAssetpermission, assetsNode);
        return permsAssetpermission;
    }

    /**
     * @param permsAssetpermission
     * @return
     */
    private void bindPermsAssetpermissionSystemUsers(PermsAssetpermission permsAssetpermission) {
        String systemuserId = getSystemuserId();
        if (!StringUtils.isEmpty(systemuserId))
            bindPermsAssetpermissionAdminSystemUsers(permsAssetpermission, systemuserId);
    }

    /**
     * 绑定 管理员系统账户
     *
     * @param permsAssetpermission
     */
    private void bindPermsAssetpermissionAdminSystemUsers(PermsAssetpermission permsAssetpermission) {
        String adminSystemuserId = getAdminSystemuserId();
        if (StringUtils.isEmpty(adminSystemuserId))
            return;
        bindPermsAssetpermissionAdminSystemUsers(permsAssetpermission, adminSystemuserId);
    }

    private void bindPermsAssetpermissionAdminSystemUsers(PermsAssetpermission permsAssetpermission, String systemuserId) {
        PermsAssetpermissionSystemUsers pre = new PermsAssetpermissionSystemUsers();
        pre.setAssetpermissionId(permsAssetpermission.getId());
        pre.setSystemuserId(systemuserId);

        PermsAssetpermissionSystemUsers permsAssetpermissionSystemUsers
                = permsAssetpermissionSystemUsersService.queryPermsAssetpermissionSystemUsersByUniqueKey(pre);
        if (permsAssetpermissionSystemUsers == null)
            permsAssetpermissionSystemUsersService.addPermsAssetpermissionSystemUsers(pre);
    }

    /**
     * 查询系统账户
     *
     * @return
     */
    private String getSystemuserId() {
        return settingBaseFacade.querySetting(SettingName.JUMPSERVER_ASSETS_SYSTEMUSER_ID);
    }

    /**
     * 查询系统账户
     *
     * @return
     */
    private String getAdminSystemuserId() {
        return settingBaseFacade.querySetting(SettingName.JUMPSERVER_ASSETS_ADMIN_SYSTEMUSER_ID);
    }

    /**
     * 授权绑定用户组
     *
     * @param permsAssetpermission
     * @param usersUsergroup
     * @return
     */
    private void bindPermsAssetpermissionUserGroups(PermsAssetpermission permsAssetpermission, UsersUsergroup usersUsergroup) {
        PermsAssetpermissionUserGroups pre = new PermsAssetpermissionUserGroups();
        pre.setAssetpermissionId(permsAssetpermission.getId());
        pre.setUsergroupId(usersUsergroup.getId());
        PermsAssetpermissionUserGroups permsAssetpermissionUserGroups = permsAssetpermissionUserGroupsServcie.queryPermsAssetpermissionUserGroupsByUniqueKey(pre);
        if (permsAssetpermissionUserGroups == null)
            permsAssetpermissionUserGroupsServcie.addPermsAssetpermissionUserGroups(pre);
    }

    /**
     * 授权绑定节点
     *
     * @param permsAssetpermission
     * @param assetsNode
     * @return
     */
    private void bindPermsAssetpermissionNodes(PermsAssetpermission permsAssetpermission, AssetsNode assetsNode) {
        PermsAssetpermissionNodes pre = new PermsAssetpermissionNodes();
        pre.setAssetpermissionId(permsAssetpermission.getId());
        pre.setNodeId(assetsNode.getId());
        PermsAssetpermissionNodes permsAssetpermissionNodes = permsAssetpermissionNodesService.queryPermsAssetpermissionNodesByUniqueKey(pre);
        if (permsAssetpermissionNodes == null)
            permsAssetpermissionNodesService.addPermsAssetpermissionNodes(pre);

    }

    @Override
    public AssetsAsset queryAssetsAssetByIp(String ip) {
        return assetsAssetService.queryAssetsAssetByIp(ip);
    }

    @Override
    public AssetsAsset queryAssetsAssetByHostname(String hostname) {
        return assetsAssetService.queryAssetsAssetByHostname(hostname);
    }

    @Override
    public void updateAssetsAsset(AssetsAsset assetsAsset) {
        assetsAssetService.updateAssetsAsset(assetsAsset);
    }

    @Override
    public void addAssetsAsset(AssetsAsset assetsAsset, String nodeId) {
        // 新增资产 写库
        assetsAssetService.addAssetsAsset(assetsAsset);
        // API
        // jumpserverAssetAPI.createAsset(assetsAsset, nodeId);
    }


    /**
     * 绑定资产到节点
     *
     * @param assetsAsset
     * @param assetsNode
     * @return
     */
    @Override
    public void bindAssetsAssetNodes(AssetsAsset assetsAsset, AssetsNode assetsNode) {
        if (assetsAsset == null || assetsNode == null) return;
        AssetsAssetNodes pre = new AssetsAssetNodes();
        pre.setAssetId(assetsAsset.getId());
        pre.setNodeId(assetsNode.getId());
        AssetsAssetNodes assetsAssetNodes = assetsAssetNodesService.queryAssetsAssetNodesByAssetId(assetsAsset.getId());
        if (assetsAssetNodes != null) {
            if (assetsAssetNodes.getNodeId().equals(assetsNode.getId()))
                return;
            assetsAssetNodesService.delAssetsAssetNodes(assetsAssetNodes.getId());
        } else {
            try {
                assetsAssetNodesService.addAssetsAssetNodes(pre);
            } catch (Exception ignored) {
            }

        }
    }

    /**
     * 资产绑定系统账户
     */
    @Override
    public void bindAvssetsSystemuserAssets(String assetId) {
        String systemuserId = getSystemuserId();
        if (StringUtils.isEmpty(systemuserId)) return;

        AssetsSystemuserAssets pre = BeanCopierUtils.copyProperties(AssetsSystemuserAssetsBO.builder()
                .systemuserId(systemuserId)
                .assetId(assetId)
                .build(), AssetsSystemuserAssets.class);

        AssetsSystemuserAssets assetsSystemuserAssets = assetsSystemuserAssetsService.queryAssetsSystemuserAssetsByUniqueKey(pre);
        if (assetsSystemuserAssets == null)
            try {
                assetsSystemuserAssetsService.addAssetsSystemuserAssets(pre);
            } catch (Exception e) {
                // 此错误不需要处理，由于通过API创建资产，不需要绑定账户
            }
    }

    @Override
    public BusinessWrapper<Boolean> delAssetsAsset(String assetId) {
        log.info("Jumpserver删除资产，assetId={}", assetId);
        try {
            // 删除资产节点绑定关系
            AssetsAssetNodes assetsAssetNodes = assetsAssetNodesService.queryAssetsAssetNodesByAssetId(assetId);
            if (assetsAssetNodes != null)
                assetsAssetNodesService.delAssetsAssetNodes(assetsAssetNodes.getId());
        } catch (Exception e) {
            log.error("删除资产节点绑定关系错误，{}", e.getMessage());
        }

        try {
            // 删除资产账户绑定
            assetsSystemuserAssetsService.deleteAssetsSystemuserAssetsByAssetId(assetId);
        } catch (Exception e) {
            log.error("删除资产账户绑定关系错误，{}", e.getMessage());
        }

        try {
            List<OpsAdhocHosts> list = opsAdhocHostsService.queryOpsAdhocHostsByAssetId(assetId);
            if (!CollectionUtils.isEmpty(list))
                list.forEach(e -> opsAdhocHostsService.deleteOpsAdhocHostsById(e.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // 删除资产账户绑定
            assetsAssetService.deleteAssetsAssetById(assetId);
            return BusinessWrapper.SUCCESS;
        } catch (Exception e) {
            log.error("删除资产账户绑定错误，{}", e.getMessage());
            return new BusinessWrapper<>(70001, "删除资产账户绑定错误!");
        }
    }

    @Override
    public boolean activeUsersUser(String username, boolean active) {
        log.info("JUMPSERVER设置用Active,username = {}, active = {}", username, active);
        UsersUser usersUser = usersUserService.queryUsersUserByUsername(username);
        if (usersUser == null)
            return true; // 账户不存在无需禁用
        usersUser.setIsActive(active);
        usersUserService.updateUsersUser(usersUser);
        return true;
    }

    @Transactional
    @Override
    public BusinessWrapper<Boolean> delUsersUser(String username) {
        log.info("JUMPSERVER删除用户,username = {}", username);
        UsersUser usersUser = usersUserService.queryUsersUserByUsername(username);
        if (usersUser == null) return BusinessWrapper.SUCCESS;
        // 删除用户/用户组关联
        usersUserGroupsService.delUsersUserGroupsByUserId(usersUser.getId());
        usersUserService.delUsersUserById(usersUser.getId());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public boolean updateUsersUser(OcUser ocUser) {
        UsersUser usersUser = usersUserService.queryUsersUserByUsername(ocUser.getUsername());
        if (usersUser == null)
            return false;
        usersUser.setFirstName(ocUser.getDisplayName());
        usersUser.setName(ocUser.getDisplayName());
        if (!StringUtils.isEmpty(ocUser.getEmail()))
            usersUser.setEmail(ocUser.getEmail());
        if (!StringUtils.isEmpty(ocUser.getPhone()))
            usersUser.setPhone(ocUser.getPhone());
        if (!StringUtils.isEmpty(ocUser.getWechat()))
            usersUser.setWechat(ocUser.getWechat());
        usersUserService.updateUsersUser(usersUser);
        return true;
    }

    @Override
    public BusinessWrapper<Boolean> setUserActive(String id) {
        UsersUser usersUser = usersUserService.queryUsersUserById(id);
        if (usersUser == null)
            return new BusinessWrapper(ErrorEnum.USER_NOT_EXIST);
        usersUser.setIsActive(!usersUser.getIsActive());
        usersUserService.updateUsersUser(usersUser);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> authAdmin(String usersUserId) {
        // 变更用户角色为Admin
        UsersUser usersUser = usersUserService.queryUsersUserById(usersUserId);
        usersUser.setRole("Admin");
        usersUserService.updateUsersUser(usersUser);
        // 用户组名称
        UsersUsergroup usersUsergroup = usersUsergroupService.queryUsersUsergroupByName(USERGROUP_ADMINISTRATORS);
        if (usersUsergroup == null)
            usersUsergroup = createUsersUsergroup(USERGROUP_ADMINISTRATORS, "Administrators");
        // 授权用户到管理员组
        assert usersUsergroup != null;
        bindUserGroups(usersUser, usersUsergroup);

        // 建立根节点绑定关系
        PermsAssetpermission permsAssetpermission = permsAssetpermissionService.queryPermsAssetpermissionByName(PERMS_ADMINISTRATORS);
        if (permsAssetpermission == null) {
            // 新增授权策略
            permsAssetpermission = PermsAssetpermissionBuilder.build(PERMS_ADMINISTRATORS);
            permsAssetpermissionService.addPermsAssetpermission(permsAssetpermission);
        }
        // 绑定管理员系统账户
        bindPermsAssetpermissionAdminSystemUsers(permsAssetpermission);
        // 绑定用户组
        bindPermsAssetpermissionUserGroups(permsAssetpermission, usersUsergroup);
        // 绑定根节点
        AssetsNode assetsNode = assetsNodeService.queryAssetsNodeRoot();
        if (assetsNode == null)
            return new BusinessWrapper(ErrorEnum.JUMPSERVER_ASSETS_NODE_ROOT_NOT_EXIST);
        bindPermsAssetpermissionNodes(permsAssetpermission, assetsNode);
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> revokeAdmin(String usersUserId) {
        // 变更用户角色为Admin
        UsersUser usersUser = usersUserService.queryUsersUserById(usersUserId);
        if (usersUser.getUsername().equals("admin"))
            return new BusinessWrapper<>(ErrorEnum.JUMPSERVER_ADMINISTRATOR_AUTHORIZATION_CANNOT_BE_REVOKED);
        usersUser.setRole("User");
        usersUserService.updateUsersUser(usersUser);
        // 用户组名称
        UsersUsergroup usersUsergroup = usersUsergroupService.queryUsersUsergroupByName(USERGROUP_ADMINISTRATORS);
        // 管理员用户组不存在
        if (usersUsergroup == null)
            return BusinessWrapper.SUCCESS;
        // 查询用户&用户组绑定关系 然后删除
        UsersUserGroups uniqueKey = new UsersUserGroups();
        uniqueKey.setUserId(usersUser.getId());
        uniqueKey.setUsergroupId(usersUsergroup.getId());

        UsersUserGroups usersUserGroups = usersUserGroupsService.queryUsersUserGroupsByUniqueKey(uniqueKey);
        if (usersUserGroups != null)
            usersUserGroupsService.delUsersUserGroupsById(usersUserGroups.getId());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public boolean checkUserPubkeyExist(String username) {
        try {
            UsersUser usersUser = usersUserService.queryUsersUserByUsername(username);
            if (usersUser == null) return false;
            if (!StringUtils.isEmpty(usersUser.getPublicKey()))
                return true;
        } catch (Exception ignored) {
        }
        return false;
    }

}
