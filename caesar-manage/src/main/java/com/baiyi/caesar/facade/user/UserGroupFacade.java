package com.baiyi.caesar.facade.user;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.user.UserGroupParam;
import com.baiyi.caesar.domain.vo.user.UserGroupVO;

/**
 * @Author baiyi
 * @Date 2021/6/16 3:20 下午
 * @Version 1.0
 */
public interface UserGroupFacade {

    DataTable<UserGroupVO.UserGroup> queryUserGroupPage(UserGroupParam.UserGroupPageQuery pageQuery);

}
