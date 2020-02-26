package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.OcUserGroup;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:59 下午
 * @Version 1.0
 */
public interface OcUserGroupService {

    DataTable<OcUserGroup> queryOcUserGroupByParam(UserGroupParam.PageQuery pageQuery);

    void addOcUserGroup(OcUserGroup ocUserGroup);

    OcUserGroup queryOcUserGroupByName(String name);

    List<OcUserGroup> queryOcUserGroupByUserId(int userId);
}
