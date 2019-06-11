package com.sdg.cmdb.dao.jumpserver;

import com.sdg.cmdb.domain.jumpserver.*;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface JumpserverDao {

    AssetsAssetDO getAssetsAssetByIp(@Param("ip") String ip);

    AssetsAssetDO getAssetsAssetByHostname(@Param("hostname") String hostname);

    int addAssetsAsset(AssetsAssetDO assetsAssetDO);

    int delAssetsAsset(@Param("id") String id);

    /**
     * 查询资产总数
     *
     * @return
     */
    int getAssetsAssetTotal();

    // List<UsersUserDO> queryUsersUserIsActive(@Param("is_active") boolean  is_active);
    /**
     * 查询闲置资产 is_active = false
     * @return
     */
    List<AssetsAssetDO> queryAssetsAssetByIsActive(@Param("is_active") boolean  is_active);

    int updateAssetsAsset(AssetsAssetDO assetsAssetDO);

    int addAssetsNode(AssetsNodeDO assetsNodeDO);

    /**
     * 校验节点NodeKey是否重复
     * @return
     */
    AssetsNodeDO getAssetsNodeLastOne();
    AssetsNodeDO getAssetsNodeByKey(@Param("key") String key);

    AssetsNodeDO getAssetsNodeByValue(@Param("value") String value);

    List<AssetsNodeDO> getAssetsNodeAll();

    int updateAssetsNode(AssetsNodeDO assetsNodeDO);

    /**
     * 查询根节点
     *
     * @return
     */
    AssetsNodeDO getAssetsNodeRoot();

    /**
     * 查询条目用于计算key
     *
     * @return
     */
    int countAssetsNode();

    AssetsAssetNodesDO getAssetsAssetNodes(AssetsAssetNodesDO assetsAssetNodesDO);

    int addAssetsAssetNodes(AssetsAssetNodesDO assetsAssetNodesDO);

    // TODO 用户/用户组
    UsersUsergroupDO getUsersUsergroupByName(@Param("name") String name);

    int addUsersUsergroup(UsersUsergroupDO usersUsergroupDO);

    PermsAssetpermissionDO getPermsAssetpermissionByName(@Param("name") String name);

    int addPermsAssetpermission(PermsAssetpermissionDO permsAssetpermissionDO);

    PermsAssetpermissionUserGroupsDO getPermsAssetpermissionUserGroups(PermsAssetpermissionUserGroupsDO permsAssetpermissionUserGroupsDO);

    int addPermsAssetpermissionUserGroups(PermsAssetpermissionUserGroupsDO permsAssetpermissionUserGroupsDO);

    PermsAssetpermissionNodesDO getPermsAssetpermissionNodes(PermsAssetpermissionNodesDO permsAssetpermissionNodesDO);

    int addPermsAssetpermissionNodes(PermsAssetpermissionNodesDO permsAssetpermissionNodesDO);

    PermsAssetpermissionSystemUsersDO getPermsAssetpermissionSystemUsers(PermsAssetpermissionSystemUsersDO permsAssetpermissionSystemUsersDO);

    int addPermsAssetpermissionSystemUsers(PermsAssetpermissionSystemUsersDO permsAssetpermissionSystemUsersDO);

    UsersUserDO getUsersUserByUsername(@Param("username") String username);

    UsersUserDO getUsersUser(@Param("id") String id);

    int delUsersUser(@Param("id") String id);


    /**
     *
     * @return
     */
    List<UsersUserDO> queryUsersUserByIsActive(@Param("is_active") boolean  is_active);

    int addUsersUser(UsersUserDO usersUserDO);

    /**
     * 查询用户总数
     *
     * @return
     */
    int getUsersUserTotal();

    int updateUsersUser(UsersUserDO usersUserDO);

    UsersUserGroupsDO getUsersUserGroups(UsersUserGroupsDO usersUserGroupsDO);

    List<UsersUserGroupsDO> queryUsersUserGroupsByUsergroupId(String usergroup_id);

    int addUsersUserGroups(UsersUserGroupsDO usersUserGroupsDO);

    int delUsersUserGroups(@Param("id") int id);

    // TODO 查询管理账户
    List<AssetsAdminuserDO> queryAssetsAdminuser(@Param("name") String name);

    AssetsAdminuserDO getAssetsAdminuser(@Param("id") String id);


    // TODO 查询系统账户
    List<AssetsSystemuserDO> queryAssetsSystemuser(@Param("name") String name);

    AssetsSystemuserDO getAssetsSystemuser(@Param("id") String id);


    // TODO 资产关联系统账户
    AvssetsSystemuserAssetsDO getAvssetsSystemuserAssets(AvssetsSystemuserAssetsDO avssetsSystemuserAssetsDO);

    int addAvssetsSystemuserAssets(AvssetsSystemuserAssetsDO avssetsSystemuserAssetsDO);

    List<TerminalDO> queryTerminal();

    /**
     * 查询当前活动会话
     *
     * @return
     */
    List<TerminalSessionDO> queryTerminalSession();

}
