package com.baiyi.opscloud.mapper.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.domain.param.jumpserver.user.UsersUserPageParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UsersUserMapper extends Mapper<UsersUser> {

    List<UsersUser> fuzzyQueryUsersUserPage(UsersUserPageParam.PageQuery pageQuery);

    List<UsersUser> fuzzyQueryAdminUsersUserPage(UsersUserPageParam.PageQuery pageQuery);
}