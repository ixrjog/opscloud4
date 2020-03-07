package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.OcServerGroupPermission;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcServerGroupPermissionMapper extends Mapper<OcServerGroupPermission> {

    List<OcServerGroupPermission> queryUserServerGroupPermission(@Param("userId") int userId);
}