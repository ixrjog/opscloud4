package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.AuthRoleMenu;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface AuthRoleMenuMapper extends Mapper<AuthRoleMenu>, InsertListMapper<AuthRoleMenu> {
}