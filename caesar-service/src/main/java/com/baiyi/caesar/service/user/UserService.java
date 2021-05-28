package com.baiyi.caesar.service.user;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.param.user.UserParam;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:26 上午
 * @Version 1.0
 */
public interface UserService {

    DataTable<User> queryPageByParam(UserParam.UserPageQuery pageQuery);

    User getByUsername(String username);

    void add(User user);

    void update(User user);

    void updateBySelective(User user);

}
