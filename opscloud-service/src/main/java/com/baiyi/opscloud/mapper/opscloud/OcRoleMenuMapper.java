package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcRoleMenu;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

public interface OcRoleMenuMapper extends Mapper<OcRoleMenu>, InsertListMapper<OcRoleMenu> {

    List<OcRoleMenu> queryOcRoleMenuByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
}