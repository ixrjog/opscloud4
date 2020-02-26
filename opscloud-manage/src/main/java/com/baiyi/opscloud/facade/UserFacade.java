package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
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

    String getRandomPassword();

    BusinessWrapper<Boolean> updateBaseUser(OcUserVO.User user);

    DataTable<OcUserGroupVO.UserGroup> queryUserGroupPage(UserGroupParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> addUserGroup(OcUserGroupVO.UserGroup userGroup);

    BusinessWrapper<Boolean> syncUserGroup();

    BusinessWrapper<Boolean> syncUser();

}
