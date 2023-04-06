package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    List<User> queryPageByParam(UserParam.UserPageQuery pageQuery);

    List<User> queryBusinessPermissionUserPageByParam(UserBusinessPermissionParam.BusinessPermissionUserPageQuery pageQuery);

    List<User> queryByTagKeys(@Param("tagKeys") List<String> tagKeys);

    List<User> queryEmployeeResignPageByParam(UserParam.EmployeeResignPageQuery pageQuery);

}