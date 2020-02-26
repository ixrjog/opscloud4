package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.OcUserPermission;
import tk.mybatis.mapper.common.Mapper;

public interface OcUserPermissionMapper extends Mapper<OcUserPermission> {

    OcUserPermission queryOcUserPermissionByUniqueKey(OcUserPermission ocUserPermission);
}