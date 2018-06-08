package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.auth.ResourceDO;
import com.sdg.cmdb.domain.auth.ResourceGroupDO;
import com.sdg.cmdb.domain.auth.RoleDO;
import com.sdg.cmdb.domain.auth.UserDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 16/9/19.
 */
@Component
public interface AuthDao {

    /**
     * 记录登录信息与对应此次登录的唯一标识
     * @param userDO
     * @return
     */
    int addUserLoginRecord(UserDO userDO);

    /**
     * 重置指定用户所有token为无效
     * @param username
     * @return
     */
    int updateUserTokenInvalid(@Param("username") String username);

    /**
     * 重置所有用户token为无效
     * @return
     */
    int updateTokenInvalid();

    /**
     * 检查token是否失效
     * @param token
     * @return
     */
    int checkTokenHasInvalid(@Param("token") String token);

    /**
     * 获取指定token对应的username
     * @param token
     * @return
     */
    String getUserByToken(@Param("token") String token);

    /**
     * 获取指定用户被授权的资源列表
     * @param username
     * @param resourceGroup
     * @return
     */
    List<ResourceDO> getUserAuthorizedResource(@Param("username") String username, @Param("resourceGroup") String resourceGroup);

    /**
     * 判断用户是否可访问某个资源
     * @param token
     * @param resourceName
     * @return
     */
    int checkUserHasResourceAuthorize(@Param("token") String token, @Param("resourceName") String resourceName);

    /**
     * 获取当前登录有效的用户列表
     * @return
     */
    List<UserDO> getLoginUsers();

    /**
     * 获取资源组数目
     * @param groupCode
     * @return
     */
    long getResourceGroupSize(@Param("groupCode") String groupCode);

    /**
     * 获取资源组分页数据
     * @param groupCode
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ResourceGroupDO> getResourceGroupPage(
            @Param("groupCode") String groupCode, @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 新增资源组信息
     * @param resourceGroupDO
     * @return
     */
    int addResourceGroup(ResourceGroupDO resourceGroupDO);

    /**
     * 更新资源组信息
     * @param resourceGroupDO
     * @return
     */
    int updateResourceGroup(ResourceGroupDO resourceGroupDO);

    /**
     * 获取指定资源组映射的资源数目
     * @param id
     * @return
     */
    int queryResourcesByGroupId(@Param("id") long id);

    /**
     * 删除指定的资源组信息
     * @param id
     * @return
     */
    int delResourceGroupByGroupId(@Param("id") long id);

    /**
     * 获取指定groupid的group信息
     * @param groupId
     * @return
     */
    ResourceGroupDO getResourceGroupByGroupId(@Param("groupId") long groupId);

    /**
     * 获取指定条件的资源数目
     * @param groupId
     * @param resourceName
     * @param authType
     * @return
     */
    long queryResourceSize(@Param("groupId") long groupId, @Param("resourceName") String resourceName, @Param("authType") int authType);

    /**
     * 获取指定条件的资源分页数据
     * @param groupId
     * @param resourceName
     * @param authType
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ResourceDO> queryResourcePage(
            @Param("groupId") long groupId, @Param("resourceName") String resourceName, @Param("authType") int authType,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 新增资源信息
     * @param resourceDO
     * @return
     */
    int addResource(ResourceDO resourceDO);

    /**
     * 新增资源&资源组关系
     * @param groupId
     * @param resourceId
     * @return
     */
    int addResourceGroupRelay(@Param("groupId") long groupId, @Param("resourceId") long resourceId);

    /**
     * 更新资源名称
     * @param resourceDO
     * @return
     */
    int updateResource(ResourceDO resourceDO);

    /**
     * 删除指定的资源
     * @param id
     * @return
     */
    int delResource(@Param("id") long id);

    /**
     * 删除指定的资源&资源组关系
     * @param resourceId
     * @return
     */
    int delResourceGroupRelay(@Param("resourceId") long resourceId);

    /**
     * 获取指定资源的角色使用情况
     * @param resourceId
     * @return
     */
    int getResourceRoleRelay(@Param("resourceId") long resourceId);

    /**
     * 获取指定资源所在的资源组
     * @param resourceId
     * @return
     */
    List<ResourceGroupDO> getGroupByResourceId(@Param("resourceId") long resourceId);

    /**
     * 获取指定资源名称的资源信息
     * @param resourceName
     * @return
     */
    ResourceDO getResourceByResourceName(@Param("resourceName") String resourceName);

    /**
     * 获取指定条件的角色数目
     * @param resourceId
     * @param roleName
     * @return
     */
    long queryRoleSize(@Param("resourceId") long resourceId, @Param("roleName") String roleName);

    /**
     * 获取指定条件的角色分页数据
     * @param resourceId
     * @param roleName
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<RoleDO> queryRolePage(
            @Param("resourceId") long resourceId, @Param("roleName") String roleName,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 新增角色
     * @param roleDO
     * @return
     */
    int addRole(RoleDO roleDO);

    /**
     * 更新角色
     * @param roleDO
     * @return
     */
    int updateRole(RoleDO roleDO);

    /**
     * 删除指定角色
     * @param id
     * @return
     */
    int delRole(@Param("id") long id);

    /**
     * 获取指定角色使用用户数
     * @param roleId
     * @return
     */
    int getRoleUsers(@Param("roleId") long roleId);

    /**
     * 获取单一条件下的role数据
     * @param roleDO
     * @return
     */
    RoleDO getRole(RoleDO roleDO);

    /**
     * 获取指定角色绑定的资源列表数目
     * @param roleId
     * @return
     */
    long getRoleBindResourceSize(@Param("roleId") long roleId, @Param("resourceGroupId")long resourceGroupId);

    /**
     * 获取指定角色绑定资源列表分页数据
     * @param roleId
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ResourceDO> getRoleBindResourcePage(
            @Param("roleId") long roleId, @Param("resourceGroupId")long resourceGroupId,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 获取指定角色未绑定的资源列表数目
     * @param roleId
     * @return
     */
    long getRoleUnbindResourceSize(@Param("roleId") long roleId, @Param("resourceGroupId")long resourceGroupId);

    /**
     * 获取指定角色未绑定的资源列表分页数据
     * @param roleId
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ResourceDO> getRoleUnbindResourcePage(
            @Param("roleId") long roleId, @Param("resourceGroupId")long resourceGroupId,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 新增角色资源的绑定关系
     * @param roleId
     * @param resourceId
     * @return
     */
    int roleResourceBind(@Param("roleId") long roleId, @Param("resourceId") long resourceId);

    /**
     * 解除角色资源的绑定关系
     * @param roleId
     * @param resourceId
     * @return
     */
    int roleResourceUnbind(@Param("roleId") long roleId, @Param("resourceId") long resourceId);

    /**
     * 获取用户数目
     * @param username
     * @param roleId
     * @return
     */
    long getUserSize(@Param("username") String username, @Param("roleId") long roleId);

    /**
     * 获取用户分页数据
     * @param username
     * @param roleId
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<UserDO> getUserPage(
            @Param("username") String username, @Param("roleId") long roleId,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * getUser
     * @param username
     * @return
     */
    List<UserDO> getUser(
            @Param("username") String username);

    /**
     * 建立用户角色绑定关系
     * @param username
     * @param roleId
     * @return
     */
    int userRoleBind(@Param("username") String username, @Param("roleId") long roleId);

    /**
     * 解除用户角色绑定关系
     * @param username
     * @param roleId
     * @return
     */
    int userRoleUnbind(@Param("username") String username, @Param("roleId") long roleId);

    /**
     * 获取用户的角色列表
     * @param username
     * @return
     */
    List<RoleDO> getUserRoles(@Param("username") String username);

    /**
     * 获取用户的角色Id列表
     * @param username
     * @return
     */
    List<Long> getUserRoleIds(@Param("username") String username);

    /**
     * 获取拥有指定角色的用户集合
     * @param roleId
     * @return
     */
    List<String> getUsersByRole(@Param("roleId") long roleId);

    /**
     * 获取指定资源组下指定用户被授权的资源集合
     * @param token
     * @param authGroupList
     * @return
     */
    List<ResourceDO> getUserAuthResources(@Param("token") String token, @Param("list")List<String> authGroupList);
}
