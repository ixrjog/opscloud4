package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.user.UserBusinessGroupParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.param.user.UserServerTreeParam;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;
import com.baiyi.opscloud.domain.vo.user.UserApiTokenVO;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;

/**
 * @Author baiyi
 * @Date 2020/2/20 11:12 上午
 * @Version 1.0
 */
public interface UserFacade {

    DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery);

    UserVO.User queryUserDetail();

    UserVO.User queryUserDetailByUsername(String username);

    DataTable<UserVO.User> fuzzyQueryUserPage(UserParam.UserPageQuery pageQuery);

    BusinessWrapper<UserApiTokenVO.UserApiToken> applyUserApiToken(UserApiTokenVO.UserApiToken userApiToken);

    BusinessWrapper<Boolean> delUserApiToken(int id);

    BusinessWrapper<UserCredentialVO.UserCredential> saveUserCredentia(UserCredentialVO.UserCredential userCredential);

    String getRandomPassword();

    BusinessWrapper<Boolean> updateBaseUser(UserParam.UpdateUser updateUser);

    BusinessWrapper<Boolean> createUser(UserParam.CreateUser createUser);

    DataTable<UserGroupVO.UserGroup> queryUserGroupPage(UserBusinessGroupParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> grantUserUserGroup(UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission);

    BusinessWrapper<Boolean> revokeUserUserGroup(UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission);

    DataTable<UserGroupVO.UserGroup> queryUserIncludeUserGroupPage(UserBusinessGroupParam.UserUserGroupPageQuery pageQuery);

    DataTable<UserGroupVO.UserGroup> queryUserExcludeUserGroupPage(UserBusinessGroupParam.UserUserGroupPageQuery pageQuery);

    BusinessWrapper<Boolean> addUserGroup(UserGroupVO.UserGroup userGroup);

    BusinessWrapper<Boolean> updateUserGroup(UserGroupVO.UserGroup userGroup);

    BusinessWrapper<Boolean> syncUserGroup();

    BusinessWrapper<Boolean> syncUser();

    ServerTreeVO.MyServerTree queryUserServerTree(UserServerTreeParam.UserServerTreeQuery userServerTreeQuery);

    /**
     * 从当前会话中查询用户
     *
     * @return
     */
    OcUser getOcUserBySession();


    /**
     * 用户离职
     * @param userId
     * @return
     */
    BusinessWrapper<Boolean> retireUser(int userId);

    BusinessWrapper<Boolean> beReinstatedUser(int userId);


}
