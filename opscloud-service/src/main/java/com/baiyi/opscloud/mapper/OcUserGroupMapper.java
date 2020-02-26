package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.OcUserGroup;
import com.baiyi.opscloud.domain.param.user.UserGroupParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcUserGroupMapper extends Mapper<OcUserGroup> {

    List<OcUserGroup> queryOcUserGroupByParam(UserGroupParam.PageQuery pageQuery);

    List<OcUserGroup> queryOcUserGroupByUserId(int userId);
}