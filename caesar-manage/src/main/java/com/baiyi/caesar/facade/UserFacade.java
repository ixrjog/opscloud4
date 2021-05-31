package com.baiyi.caesar.facade;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;
import com.baiyi.caesar.domain.param.user.UserParam;
import com.baiyi.caesar.vo.server.ServerTreeVO;
import com.baiyi.caesar.vo.user.UserVO;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:38 上午
 * @Version 1.0
 */
public interface UserFacade {

    DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery);

    void addUser(UserVO.User user);

    void updateUser(UserVO.User user);

    ServerTreeVO.ServerTree queryUserServerTree(ServerGroupParam.UserServerTreeQuery queryParam);
}
