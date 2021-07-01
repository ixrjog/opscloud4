package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.user.UserParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:26 上午
 * @Version 1.0
 */
public interface UserService {

    User getById(Integer id);

    DataTable<User> queryPageByParam(UserParam.UserPageQuery pageQuery);

    User getByUsername(String username);

    void add(User user);

    void update(User user);

    void updateBySelective(User user);

    List<User> listActive();

}
