package com.baiyi.caesar.mapper.caesar;

import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.param.user.UserParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    List<User> queryPageByParam(UserParam.UserPageQuery pageQuery);
}