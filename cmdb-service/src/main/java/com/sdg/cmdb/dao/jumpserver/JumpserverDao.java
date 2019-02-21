package com.sdg.cmdb.dao.jumpserver;


import com.sdg.cmdb.domain.jumpserver.*;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface JumpserverDao {


    AssetsAssetDO getAssetsAssetByIp(@Param("ip") String ip);

    int addAssetsAsset(AssetsAssetDO assetsAssetDO);

    int updateAssetsAsset(AssetsAssetDO assetsAssetDO);


    int addAssetsNode(AssetsNodeDO assetsNodeDO);

    AssetsNodeDO getAssetsNodeByValue(@Param("value") String value);

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

    int addUsersUser(UsersUserDO usersUserDO);


    UsersUserGroupsDO getUsersUserGroups(UsersUserGroupsDO usersUserGroupsDO);

    int addUsersUserGroups(UsersUserGroupsDO usersUserGroupsDO);

    // TODO 查询管理账户
    List<AssetsAdminuserDO> queryAssetsAdminuser(@Param("name") String name);
    AssetsAdminuserDO getAssetsAdminuser(@Param("id") String id);


    // TODO 查询系统账户
    List<AssetsSystemuserDO> queryAssetsSystemuser(@Param("name") String name);
    AssetsSystemuserDO getAssetsSystemuser(@Param("id") String id);





}
