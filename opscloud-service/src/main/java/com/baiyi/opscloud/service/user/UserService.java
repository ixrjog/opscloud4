package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.user.UserParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:26 上午
 * @Version 1.0
 */
public interface UserService {

    User getById(Integer id);

    List<User> queryAll();

    DataTable<User> queryPageByParam(UserParam.UserPageQuery pageQuery);

    DataTable<User> queryPageByParam(UserBusinessPermissionParam.BusinessPermissionUserPageQuery pageQuery);

    User getByUsername(String username);

    void add(User user);

    void update(User user);

    /**
     * 登录更新接口，不触发事件
     * @param user
     */
    void updateLogin(User user);

    void delete(User user);

    void updateBySelective(User user);

    List<User> listActive();

    List<User> listInactive();

}
