package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserLeaveDO;
import com.sdg.cmdb.domain.auth.UserLeaveVO;
import com.sdg.cmdb.domain.auth.UserVO;


import java.util.List;

/**
 * Created by zxxiao on 2016/11/22.
 */
public interface UserService {

    /**
     * 堡垒机分页数据查询
     *
     * @param username
     * @param page
     * @param length
     * @return
     */
    TableVO<List<UserDO>> getUserPage(String username, int page, int length);


    TableVO<List<UserVO>> getSafeUserPage(String username, int page, int length);


    /**
     * 堡垒机分页数据查询
     *
     * @param username
     * @param page
     * @param length
     * @return
     */
    TableVO<List<UserVO>> getCmdbUserPage(String username, int page, int length);

    /**
     * 获取单个用户详细信息（权限）
     *
     * @param username
     * @return
     */
    TableVO<UserVO> getCmdbUser(String username);


    /**
     * 离职用户分页数据查询
     *
     * @param username
     * @param page
     * @param length
     * @return
     */
    TableVO<List<UserLeaveVO>> getUserLeavePage(String username, int page, int length);

    /**
     * 删除离职用户
     *
     * @param id
     * @return
     */
    BusinessWrapper<Boolean> delUserLeave(long id);


    /**
     * 检查用户是否存在
     *
     * @param username
     * @return
     */
    UserDO getUserDOByName(String username);

    UserDO getUserByDO(UserDO userDO);

    /**
     * 获取用户信息
     *
     * @param username
     * @return
     */
    BusinessWrapper<UserVO> getUserVOByName(String username);

    /**
     * 更新用户信息
     *
     * @param userDO
     * @return
     */
    BusinessWrapper<Boolean> saveUserInfo(UserDO userDO);

    /**
     * 更新指定用户的授权状态
     *
     * @param userDO
     * @return
     */
    boolean updateUserAuthStatus(UserDO userDO);

    boolean saveUserLeave(UserLeaveDO userLeaveDO);

    /**
     * 查询员工的手机号
     *
     * @param
     * @return
     */
    String queryUserMobileByEmail(String email);

    BusinessWrapper<Boolean> addUsersMobile();

    BusinessWrapper<Boolean> addUserMobile(long userId);

    /**
     * 查询不在应用中的用户列表
     * @param username
     * @param appId
     * @return
     */
    List<UserVO> queryUserExcludeApp(String username, long appId);

    List<UserVO> queryUserByApp(long appId);

    BusinessWrapper<Boolean>  addUserByApp(long appId,long userId);

    BusinessWrapper<Boolean>  delUserByApp(long appId,long userId);

    /**
     * @param displayName username<displayName>
     * @return
     */
    UserDO getUserByDisplayName(String displayName);
}
