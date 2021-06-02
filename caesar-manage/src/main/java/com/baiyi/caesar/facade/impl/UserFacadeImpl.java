package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.common.exception.common.CommonRuntimeException;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.param.server.ServerGroupParam;
import com.baiyi.caesar.domain.param.user.UserParam;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.facade.server.ServerGroupFacade;
import com.baiyi.caesar.packer.user.UserPacker;
import com.baiyi.caesar.service.user.UserService;
import com.baiyi.caesar.domain.vo.server.ServerTreeVO;
import com.baiyi.caesar.domain.vo.user.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/14 10:38 上午
 * @Version 1.0
 */
@Service
public class UserFacadeImpl implements UserFacade {

    @Resource
    private UserService userService;

    @Resource
    private UserPacker userPacker;

    @Resource
    private ServerGroupFacade serverGroupFacade;

    @Override
    public DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery) {
        DataTable<User> table = userService.queryPageByParam(pageQuery);
        return new DataTable<>(userPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public void addUser(UserVO.User user) {
        User pre = userPacker.toDO(user);
        if (StringUtils.isEmpty(pre.getPassword()))
            throw new CommonRuntimeException("密码不能为空");
        userService.add(pre);
    }

    @Override
    public void updateUser(UserVO.User user) {
        User pre = userPacker.toDO(user);
        userService.updateBySelective(pre);
    }

    @Override
    public ServerTreeVO.ServerTree queryUserServerTree(ServerGroupParam.UserServerTreeQuery queryParam) {
        User user = userService.getByUsername(SessionUtil.getUsername());
        queryParam.setUserId(user.getId());
        return serverGroupFacade.queryServerTree(queryParam, user);
    }


}
