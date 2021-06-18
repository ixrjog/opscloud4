package com.baiyi.caesar.service.user;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.UserGroup;
import com.baiyi.caesar.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.caesar.domain.param.user.UserGroupParam;

/**
 * @Author baiyi
 * @Date 2021/6/16 3:16 下午
 * @Version 1.0
 */
public interface UserGroupService {

    void add(UserGroup userGroup);

    void update(UserGroup userGroup);

    DataTable<UserGroup> queryPageByParam(UserGroupParam.UserGroupPageQuery pageQuery);

    DataTable<UserGroup> queryPageByParam(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery);
}
