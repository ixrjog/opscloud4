package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.*;
import com.sdg.cmdb.domain.server.ServerGroupDO;

import java.util.List;

/**
 * 授权验证服务
 * Created by zxxiao on 16/9/18.
 */
public interface AuthService {

    /**
     * 登录校验
     *
     * @param username
     * @param password
     * @return
     */
    BusinessWrapper<UserDO> loginCredentialCheck(String username, String password);

    /**
     * api 登陆校验
     *
     * @param userDO
     * @return
     */
    HttpResult apiLoginCheck(UserDO userDO);

    /**
     * 登出
     *
     * @param username
     * @return
     */
    BusinessWrapper<Boolean> logout(String username);

    /**
     * 获取指定token对应的username
     *
     * @param token
     * @return
     */
    String getUserByToken(String token);

    /**
     * 判断用户是否可访问某个资源
     *
     * @param token
     * @param resourceName
     * @return
     */
    BusinessWrapper<Boolean> checkUserHasResourceAuthorize(String token, String resourceName);

    /**
     * 判断用户是否可访问某个资源,如果可以,返回该资源下的子资源集合
     *
     * @param token
     * @param resourceName
     * @param authGroupList
     * @return
     */
    BusinessWrapper<List<ResourceDO>> checkAndGetUserHasResourceAuthorize(String token, String resourceName, List<String> authGroupList);

    /**
     * 获取满足要求的资源组数据
     *
     * @param groupCode
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ResourceGroupDO>> getResourceGroups(String groupCode, int page, int length);

    /**
     * 保存 || 更新资源组信息
     *
     * @param resourceGroupDO
     * @return
     */
    BusinessWrapper<Boolean> saveResourceGroup(ResourceGroupDO resourceGroupDO);

    /**
     * 删除指定的资源组id
     *
     * @param groupId
     * @return
     */
    BusinessWrapper<Boolean> delResourceGroup(long groupId);

    /**
     * 获取合适条件的资源数据集合
     *
     * @param groupId
     * @param resourceName
     * @param authType
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ResourceVO>> getResources(long groupId, String resourceName, int authType, int page, int length);

    /**
     * 保存 || 更新资源信息
     *
     * @param resourceVO
     * @return
     */
    BusinessWrapper<Boolean> saveResource(ResourceVO resourceVO);

    /**
     * 删除指定的资源id
     *
     * @param resourceId
     * @return
     */
    BusinessWrapper<Boolean> delResource(long resourceId);

    /**
     * 获取指定id的role
     *
     * @param roleId
     * @return
     */
    RoleDO getRoleById(long roleId);

    /**
     * 获取角色集合
     *
     * @param resourceId
     * @param roleName
     * @param page
     * @param length
     * @return
     */
    TableVO<List<RoleDO>> getRoles(long resourceId, String roleName, int page, int length);


    /**
     * 判断用户是否是某个角色
     *
     * @param username
     * @param roleName
     * @return
     */
    boolean isRole(String username, String roleName);


    /**
     * 新增 || 更新角色信息
     *
     * @param roleDO
     * @return
     */
    BusinessWrapper<Boolean> saveRole(RoleDO roleDO);

    /**
     * 删除指定的角色
     *
     * @param roleId
     * @return
     */
    BusinessWrapper<Boolean> delRole(long roleId);

    /**
     * 获取指定角色已绑定的资源集合
     *
     * @param roleId
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ResourceVO>> getRoleBindResources(long roleId, long resourceGroupId, int page, int length);

    /**
     * 获取指定角色未绑定的资源集合
     *
     * @param roleId
     * @param page
     * @param length
     * @return
     */
    TableVO<List<ResourceVO>> getRoleUnbindResources(long roleId, long resourceGroupId, int page, int length);

    /**
     * 新增角色资源的绑定关系
     *
     * @param roleId
     * @param resourceId
     * @return
     */
    BusinessWrapper<Boolean> roleResourceBind(long roleId, long resourceId);

    /**
     * 解除角色资源的绑定关系
     *
     * @param roleId
     * @param resourceId
     * @return
     */
    BusinessWrapper<Boolean> roleResourceUnbind(long roleId, long resourceId);

    /**
     * 获取指定条件的用户角色数据
     *
     * @param username
     * @param roleId
     * @param page
     * @param length
     * @return
     */
    TableVO<List<UserVO>> getUsers(String username, long roleId, int page, int length);

    /**
     * 建立用户角色绑定关系
     *
     * @param roleId
     * @param username
     * @return
     */
    BusinessWrapper<Boolean> userRoleBind(long roleId, String username);

    /**
     * opscloud 绑定用户到某个角色
     *
     * @param roleName
     * @param username
     * @return
     */
    BusinessWrapper<Boolean> userRoleBind(String roleName, String username);

    /**
     * 解除用户角色绑定关系
     *
     * @param roleId
     * @param username
     * @return
     */
    BusinessWrapper<Boolean> userRoleUnbind(long roleId, String username);

    BusinessWrapper<Boolean> userRoleUnbind(String roleName, String username);

    /**
     * 获取指定用户所拥有的可见组权限
     *
     * @param username
     * @return
     */
    List<String> getUserGroup(String username);

    /**
     * 获取指定用户所拥有的可见组权限
     *
     * @param username
     * @return
     */
    List<Long> getUserGroupIds(String username);

    /**
     * 获取用户的角色Id列表
     *
     * @param username
     * @return
     */
    List<Long> getUserRoleIds(String username);


    /**
     * 获取拥有指定角色的用户集合
     *
     * @param roleId
     * @return
     */
    List<UserDO> getUsersByRole(long roleId);

    /**
     * 获取拥有指定角色的用户集合
     *
     * @param roleName
     * @return
     */
    List<UserVO> getUsersByRole(String roleName);

    /**
     * 删除离职用户
     *
     * @param username
     */
    BusinessWrapper<Boolean> delUser(String username);


    /**
     * 重置token
     *
     * @return
     */
    BusinessWrapper<Boolean> resetToken();

    BusinessWrapper<Boolean> resetToken(String username);

    /**
     * 添加一个堡垒机用户
     *
     * @param userVO
     * @return
     */
    BusinessWrapper<Boolean> addUser(UserVO userVO);

    /**
     * 按角色名称查询所有用户(工单系统使用)
     *
     * @param roleName
     * @return
     */
    List<UserDO> queryUsersByRoleName(String roleName);

}
