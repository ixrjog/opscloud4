package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.builder.AssetsAssetBuilder;
import com.baiyi.opscloud.common.base.CredentialType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.jumpserver.*;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.user.OcUserCredentialVO;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;
import com.baiyi.opscloud.facade.JumpserverFacade;
import com.baiyi.opscloud.facade.ServerAttributeFacade;
import com.baiyi.opscloud.facade.ServerFacade;
import com.baiyi.opscloud.jumpserver.center.JumpserverCenter;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/8 4:18 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class JumpserverFacadeImpl implements JumpserverFacade {

    @Resource
    private OcServerService ocServerService;

    @Resource
    private ServerFacade serverFacade;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private JumpserverCenter jumpserverCenter;

    @Resource
    private ServerAttributeFacade serverAttributeFacade;

    // 授权规则前缀
    public final String PERMS_PREFIX = "perms_";

    // 过期时间
    //public final String DATE_EXPIRED = "2089-01-01 00:00:00";

    // 管理员用户组 绑定 根节点
    public final String USERGROUP_ADMINISTRATORS = "usergroup_administrators";
    // 管理员授权
    public final String PERMS_ADMINISTRATORS = "perms_administrators";

    @Override
    public BusinessWrapper<Boolean> syncUsers() {
        List<OcUser> userList = ocUserService.queryOcUserActive();
        for (OcUser ocUser : userList)
            bindUserGroups(ocUser);
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
        List<OcServerGroup> serverGroupList = ocServerGroupService.queryUerPermissionOcServerGroupByUserId(ocUser.getId());
        // 创建用户
        UsersUser usersUser = jumpserverCenter.createUsersUser(ocUser);
        for (OcServerGroup ocServerGroup : serverGroupList)
            jumpserverCenter.bindUserGroups(usersUser, ocServerGroup);
    }

    /**
     * 新增资产
     *
     * @param ocServer
     */
    @Override
    public void addAssets(OcServer ocServer) {
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(ocServer.getServerGroupId());
        // 创建资产节点（服务器组）
        AssetsNode assetsNode = jumpserverCenter.createAssetsNode(ocServerGroup);
        if (assetsNode == null) {
            log.error("Jumpserver 同步节点（服务器组）{} Error !", ocServerGroup.getName());
            return;
        }
        // 创建用户组
        UsersUsergroup usersUsergroup = jumpserverCenter.createUsersUsergroup(ocServerGroup);

        // 创建授权并绑定 节点，用户组，系统账户
        PermsAssetpermission permsAssetpermission = jumpserverCenter.createPermsAssetpermission(ocServerGroup, assetsNode, usersUsergroup);
        // 创建资产（主机）
        AssetsAsset assetsAsset = createAssetsAsset(ocServer, "");
        // 绑定资产到节点
        jumpserverCenter.bindAssetsAssetNodes(assetsAsset, assetsNode);
        // 资产绑定系统账户
        jumpserverCenter.bindAvssetsSystemuserAssets(assetsAsset.getId());
    }

    /**
     * 创建资产（主机）
     * 需要解决一个问题 公网链接服务器删除内网服务器
     *
     * @param ocServer
     * @return
     */
    private AssetsAsset createAssetsAsset(OcServer ocServer, String comment) {
        if (StringUtils.isEmpty(comment))
            comment = "";
        // Kubernetes服务器增加标签
        // String label = getServerLabel(serverDO);
        // if (!StringUtils.isEmpty(label))
        //    comment = label;
        String adminUserId = getAdminuserId();
        if (StringUtils.isEmpty(adminUserId))
            return null;
        AssetsAsset assetsAsset = getAssetsAsset(ocServer);
        // 更新服务器信息
        if (assetsAsset != null) {
            if (!StringUtils.isEmpty(comment)) {
                assetsAsset.setComment(comment);
            } else {
                assetsAsset.setComment(ocServer.getComment());
            }
            if (!StringUtils.isEmpty(ocServer.getPublicIp()))
                assetsAsset.setComment(Joiner.on("").join(assetsAsset.getComment(), "(pubIp:", ocServer.getPublicIp(), ")"));
            assetsAsset.setHostname(getHostName(ocServer));
            //assetsAssetDO.setCreated_by("oc auto");
            jumpserverCenter.updateAssetsAsset(assetsAsset);
        } else {
            String manageIp = serverAttributeFacade.getManageIp(ocServer);
            assetsAsset = AssetsAssetBuilder.build(ocServer, manageIp, adminUserId, getHostName(ocServer));
            if (!StringUtils.isEmpty(comment))
                assetsAsset.setComment(comment);
            jumpserverCenter.addAssetsAsset(assetsAsset);
        }
        return assetsAsset;
    }

    private String getAdminuserId() {
        // TODO return cacheKeyService.getKeyByString(this.getClass(), "AdminuserId");
        return null;
    }


    private AssetsAsset getAssetsAsset(OcServer ocServer) {
        String manageIp = serverAttributeFacade.getManageIp(ocServer);
        AssetsAsset assetsAsset = jumpserverCenter.queryAssetsAssetByIp(manageIp);
        if (assetsAsset != null) return assetsAsset;

        // 主机名查询服务器
        assetsAsset = jumpserverCenter.queryAssetsAssetByHostname(getHostName(ocServer));
        if (assetsAsset != null) {
            // 更新IP 此服务器可能是使用公网连接
            assetsAsset.setIp(manageIp);
            jumpserverCenter.updateAssetsAsset(assetsAsset);
            return assetsAsset;
        }
        return assetsAsset;
    }

    /**
     * 取主机名称 序号填充对齐
     * 例如 当前主机数量为80台, 第一台序号为 01
     *
     * @param ocServer
     * @return
     */
    private String getHostName(OcServer ocServer) {
        int serverCount = ocServerService.countByServerGroupId(ocServer.getServerGroupId());
        String format = Joiner.on("").join("%0", String.valueOf(serverCount).length(), "d");
        String sn = String.format(format, ocServer.getSerialNumber());
        return Joiner.on("-").join(serverFacade.acqHostname(ocServer), sn);
    }

    /**
     * 用户授权用户组
     *
     * @param ocUser
     * @param ocServerGroup
     */
    @Override
    public void grant(OcUser ocUser, OcServerGroup ocServerGroup) {
        // 创建用户
        UsersUser usersUser = jumpserverCenter.createUsersUser(ocUser);
        // 用户绑定组
        jumpserverCenter.bindUserGroups(usersUser, ocServerGroup);
    }

    /**
     * 用户撤销用户组
     *
     * @param ocUser
     * @param ocServerGroup
     */
    @Override
    public void revoke(OcUser ocUser, OcServerGroup ocServerGroup) {
        jumpserverCenter.revoke(ocUser, ocServerGroup);
    }

    @Override
    public boolean activeUsersUser(String username, boolean active) {
        return jumpserverCenter.activeUsersUser(username, active);
    }

    @Override
    public boolean delUsersUser(String username) {
        return jumpserverCenter.delUsersUser(username);
    }

    @Override
    public boolean updateUsersUser(OcUser ocUser) {
        return jumpserverCenter.updateUsersUser(ocUser);
    }

    @Override
    public boolean pushKey(OcUserVO.User user) {
        //OcUserCredential sshKeyCredential = ocUserService.queryOcUserByUsername(ocUser.getUsername());
        OcUser ocUser = BeanCopierUtils.copyProperties(user, OcUser.class);
        String key = CredentialType.SSH_PUB_KEY.getName();
        if (!user.getCredentialMap().containsKey(key))
            return false;
        OcUserCredentialVO.UserCredential credential = user.getCredentialMap().get(key);
        return jumpserverCenter.pushKey(ocUser, credential);
    }


}
