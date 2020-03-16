package com.baiyi.opscloud.jumpserver.center;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.jumpserver.*;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.user.OcUserCredentialVO;

/**
 * @Author baiyi
 * @Date 2020/1/9 11:36 上午
 * @Version 1.0
 */
public interface JumpserverCenter {

    /**
     * 用户绑定服务器组
     *
     * @param usersUser
     * @param ocServerGroup
     */
    void bindUserGroups(UsersUser usersUser, OcServerGroup ocServerGroup);

    /**
     * 创建用户
     *
     * @param ocUser
     * @return
     */
    UsersUser saveUsersUser(OcUser ocUser);

    AssetsNode createAssetsNode(OcServerGroup ocServerGroup);

    /**
     * 创建用户组
     *
     * @param ocServerGroup
     * @return
     */
    UsersUsergroup saveUsersUsergroup(OcServerGroup ocServerGroup);

    /**
     * 创建授权策略
     *
     * @param ocServerGroup
     * @param assetsNode
     * @param usersUsergroup
     * @return
     */
    PermsAssetpermission createPermsAssetpermission(OcServerGroup ocServerGroup, AssetsNode assetsNode, UsersUsergroup usersUsergroup);

    AssetsAsset queryAssetsAssetByIp(String ip);

    AssetsAsset queryAssetsAssetByHostname(String hostname);

    void updateAssetsAsset(AssetsAsset assetsAsset);

    void addAssetsAsset(AssetsAsset assetsAsset);

    void bindAssetsAssetNodes(AssetsAsset assetsAsset, AssetsNode assetsNode);

    void bindAvssetsSystemuserAssets(String assetId);

    Boolean grant(OcUser ocUser, String resource);

    Boolean revoke(OcUser ocUser, String resource);

    boolean activeUsersUser(String username, boolean active);

    boolean delUsersUser(String username);

    UsersUser createUsersUser(OcUser ocUser);

    boolean updateUsersUser(OcUser ocUser);

    boolean pushKey(OcUser ocUser, OcUserCredentialVO.UserCredential credential);

    BusinessWrapper<Boolean> setUserActive(String id);

    BusinessWrapper<Boolean> authAdmin(String usersUserId);

    BusinessWrapper<Boolean> revokeAdmin(String usersUserId);

    /**
     * 校验用户公钥是否存在
     * @param username
     * @return
     */
    boolean checkUserPubkeyExist(String username);

}
