package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.UserGroup;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;

/**
 * @Author baiyi
 * @Date 2021/6/16 3:16 下午
 * @Version 1.0
 */
public interface UserGroupService {

    void add(UserGroup userGroup);

    void update(UserGroup userGroup);

    void deleteById(Integer id);

    UserGroup getByName(String name);

    UserGroup getById(Integer id);

    DataTable<UserGroup> queryPageByParam(UserGroupParam.UserGroupPageQuery pageQuery);

    DataTable<UserGroup> queryPageByParam(UserBusinessPermissionParam.UserBusinessPermissionPageQuery pageQuery);

}