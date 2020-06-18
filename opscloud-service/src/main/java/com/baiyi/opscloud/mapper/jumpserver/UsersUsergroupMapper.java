package com.baiyi.opscloud.mapper.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.UsersUsergroup;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UsersUsergroupMapper extends Mapper<UsersUsergroup> {

    List<UsersUsergroup> queryUsersUsergroupByUsername(String username);
}