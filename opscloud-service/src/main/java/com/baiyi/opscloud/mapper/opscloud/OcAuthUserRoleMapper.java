package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAuthUserRole;
import com.baiyi.opscloud.domain.param.auth.UserRoleParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcAuthUserRoleMapper extends Mapper<OcAuthUserRole> {

    List<OcAuthUserRole> queryOcAuthUserRoleByParam(UserRoleParam.UserRolePageQuery pageQuery);

    int authenticationByUsernameAndResourceName(@Param("username") String username, @Param("resourceName") String resourceName);

}