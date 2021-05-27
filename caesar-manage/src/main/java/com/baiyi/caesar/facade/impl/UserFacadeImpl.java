package com.baiyi.caesar.facade.impl;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.param.user.UserParam;
import com.baiyi.caesar.facade.UserFacade;
import com.baiyi.caesar.packer.user.UserPacker;
import com.baiyi.caesar.service.user.UserService;
import com.baiyi.caesar.vo.user.UserVO;
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

    @Override
    public DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery) {
        DataTable<User> table = userService.queryPageByParam(pageQuery);
        return new DataTable<>(userPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

}
