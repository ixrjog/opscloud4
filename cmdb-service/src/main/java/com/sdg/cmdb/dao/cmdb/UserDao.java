package com.sdg.cmdb.dao.cmdb;

import com.sdg.cmdb.domain.auth.CiUserDO;
import com.sdg.cmdb.domain.auth.CiUserGroupDO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserLeaveDO;
import com.sdg.cmdb.domain.systems.SystemDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 2016/11/22.
 */
@Component
public interface UserDao {

    /**
     * 保存用户信息
     *
     * @param userDO
     * @return
     */
    int saveUserInfo(UserDO userDO);

    /**
     * 更新用户授权信息
     *
     * @param userDO
     * @return
     */
    int updateUserAuth(UserDO userDO);

    /**
     * 获取指定用户名的数目
     *
     * @param username
     * @return
     */
    long getUserSize(@Param("username") String username);

    /**
     * 获取指定用户名的分页数据
     *
     * @param username
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<UserDO> getUserPage(
            @Param("username") String username,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);


    /**
     * 获取指定持续集成用户组的分页数据
     *
     * @param groupName
     * @param envType
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<CiUserGroupDO> getCiUserGroupPage(
            @Param("groupName") String groupName,
            @Param("envType") int envType,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    CiUserGroupDO getCiUserGroupByNameAndEnvType(
            @Param("groupName") String groupName,
            @Param("envType") int envType);

    CiUserGroupDO getCiUserGroupByServerGroupIdAndEnvType(
            @Param("serverGroupId") long serverGroupId,
            @Param("envType") int envType);


    CiUserGroupDO getCiUserGroupById(
            @Param("id") long id);


    /**
     * 新增持续集成用户组数据
     *
     * @param ciUserGroupDO
     * @return
     */
    int addCiUserGroup(CiUserGroupDO ciUserGroupDO);

    int delCiUserGroup(@Param("id") long id);

    int updateCiUserGroup(CiUserGroupDO ciUserGroupDO);

    /**
     * 获取指定持续集成用户组的数目
     *
     * @param groupName
     * @param envType
     * @return
     */
    long getCiUserGroupSize(@Param("groupName") String groupName,
                            @Param("envType") int envType);


    /**
     * 获取指定用户名的用户信息
     *
     * @param username
     * @return
     */
    UserDO getUserByName(@Param("username") String username);

    UserDO getUserById(@Param("id") long id);

    /**
     * 更新指定用户的密码
     *
     * @param userDO
     * @return
     */
    int updateUser(UserDO userDO);

    /**
     * 更新指定用户的zabbix授权
     *
     * @param userDO
     * @return
     */
    int updateUserZabbixAuthed(UserDO userDO);

    /**
     * 删除用户
     *
     * @param username
     * @return
     */
    int delUser(@Param("username") String username);

    /**
     * 获取所有用户
     *
     * @return
     */
    List<UserDO> getAllUser();

    /**
     * 按用户名查询显示名
     *
     * @param username
     * @return
     */
    String getDisplayNameByUserName(@Param("username") String username);


    /**
     * 新增用户离职数据
     *
     * @param userLeaveDO
     * @return
     */
    int addUserLeave(UserLeaveDO userLeaveDO);


    /**
     * 更新用户离职数据
     *
     * @param userLeaveDO
     * @return
     */
    int updateUserLeave(UserLeaveDO userLeaveDO);


    /**
     * 获取指定用户名的用户离职分页数据
     *
     * @param username
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<UserLeaveDO> getUserLeavePage(
            @Param("username") String username,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);


    /**
     * 获取指定用户名的离职用户数目
     *
     * @param username
     * @return
     */
    long getUserLeaveSize(@Param("username") String username);

    long delUserLeave(@Param("id") long id);

    /**
     * 获取持续集成用户权限数据
     *
     * @param userId
     * @return
     */
    List<CiUserDO> getCiUserByUserId(
            @Param("userId") long userId);

    CiUserDO getCiUserById(
            @Param("id") long id);


    CiUserDO getCiUserByUserIdAndusergroupId(
            @Param("userId") long userId, @Param("usergroupId") long usergroupId);

    int addCiUser(CiUserDO ciUserDO);

    int delCiUser(@Param("id") long id);

}
