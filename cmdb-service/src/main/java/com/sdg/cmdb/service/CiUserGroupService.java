package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.auth.CiUserGroupVO;
import com.sdg.cmdb.domain.auth.UserVO;

import java.util.List;

/**
 * Created by liangjian on 2017/8/17.
 */
public interface CiUserGroupService {

    TableVO<List<CiUserGroupVO>> getCiUserGroupPage(String groupName, int envType, int page, int length);


    BusinessWrapper<Boolean> groupsRefresh();

    BusinessWrapper<Boolean> delCigroup(long id);

    /**
     * 持续集成用户分页数据查询
     * @param username
     * @param page
     * @param length
     * @return
     */
    TableVO<List<UserVO>> getCiUserPage(String username, int page, int length);

    UserVO getCiUser(String username);


    BusinessWrapper<Boolean> usersRefresh();


    BusinessWrapper<Boolean> saveCiUserGroup(CiUserGroupVO ciUserGroupVO);

    /**
     * 添加用户的持续集成权限组
     * @param userId
     * @param serverGroupName
     * @return
     */
    boolean userAddGroup(long userId, String serverGroupName);

    /**
     * 添加用户的持续集成权限组
     * @param userId
     * @param usergroupId
     * @return
     */
    BusinessWrapper<Boolean> userAddGroup(long userId, long usergroupId);

    /**
     * 删除用户的持续集成权限组
     * @param ciuserId
     * @return
     */
    BusinessWrapper<Boolean> userDelGroup( long ciuserId);
}
