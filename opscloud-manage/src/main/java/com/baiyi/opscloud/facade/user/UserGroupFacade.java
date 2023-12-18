package com.baiyi.opscloud.facade.user;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;

/**
 * @Author baiyi
 * @Date 2021/6/16 3:20 下午
 * @Version 1.0
 */
public interface UserGroupFacade {

    DataTable<UserGroupVO.UserGroup> queryUserGroupPage(UserGroupParam.UserGroupPageQuery pageQuery);

    void addUserGroup(UserGroupParam.UserGroup userGroup);

    void updateUserGroup(UserGroupParam.UserGroup userGroup);

    void deleteUserGroupById(Integer id);
}
