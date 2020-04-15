package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.user.UserBusinessGroupParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.param.user.UserServerTreeParam;
import com.baiyi.opscloud.domain.vo.server.ServerTreeVO;
import com.baiyi.opscloud.domain.vo.user.OcUserApiTokenVO;
import com.baiyi.opscloud.domain.vo.user.OcUserCredentialVO;
import com.baiyi.opscloud.domain.vo.user.OcUserGroupVO;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;

/**
 * @Author baiyi
 * @Date 2020/2/20 11:12 上午
 * @Version 1.0
 */
public interface UserFacade {

    DataTable<OcUserVO.User> queryUserPage(UserParam.PageQuery pageQuery);

    OcUserVO.User queryUserDetail();

    DataTable<OcUserVO.User> fuzzyQueryUserPage(UserParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> applyUserApiToken(OcUserApiTokenVO.UserApiToken userApiToken);

    BusinessWrapper<Boolean> delUserApiToken(int id);

    BusinessWrapper<Boolean> saveUserCredentia(OcUserCredentialVO.UserCredential userCredential);

    String getRandomPassword();

    BusinessWrapper<Boolean> updateBaseUser(OcUserVO.User user);

    BusinessWrapper<Boolean> createUser(OcUserVO.User user);

    DataTable<OcUserGroupVO.UserGroup> queryUserGroupPage(UserBusinessGroupParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> grantUserUserGroup(UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission);

    BusinessWrapper<Boolean> revokeUserUserGroup(UserBusinessGroupParam.UserUserGroupPermission userUserGroupPermission);

    DataTable<OcUserGroupVO.UserGroup> queryUserIncludeUserGroupPage(UserBusinessGroupParam.UserUserGroupPageQuery pageQuery);

    DataTable<OcUserGroupVO.UserGroup> queryUserExcludeUserGroupPage(UserBusinessGroupParam.UserUserGroupPageQuery pageQuery);

    BusinessWrapper<Boolean> addUserGroup(OcUserGroupVO.UserGroup userGroup);

    BusinessWrapper<Boolean> syncUserGroup();

    BusinessWrapper<Boolean> syncUser();

    ServerTreeVO.MyServerTree queryUserServerTree(UserServerTreeParam.UserServerTreeQuery userServerTreeQuery);

}
