package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcUserGroup;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcUserGroupMapper extends Mapper<OcUserGroup> {

    List<OcUserGroup> queryOcUserGroupByParam(UserGroupParam.PageQuery pageQuery);

    List<OcUserGroup> queryOcUserGroupByUserId(int userId);

    // 用户授权用户组
    List<OcUserGroup> queryUserOcUserGroupByParam(UserGroupParam.UserUserGroupPageQuery pageQuery);

    // 用户未授权用户组
    List<OcUserGroup> queryUserExcludeOcUserGroupByParam(UserGroupParam.UserUserGroupPageQuery pageQuery);

}