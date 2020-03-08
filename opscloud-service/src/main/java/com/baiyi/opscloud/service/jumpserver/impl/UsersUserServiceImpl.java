package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.mapper.jumpserver.UsersUserMapper;
import com.baiyi.opscloud.service.jumpserver.UsersUserService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/8 4:42 下午
 * @Version 1.0
 */

@Service
public class UsersUserServiceImpl implements UsersUserService {

    @Resource
    private UsersUserMapper usersUserMapper;

    @Override
    public UsersUser queryUsersUserByUsername(String username) {
        Example example = new Example(UsersUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return usersUserMapper.selectOneByExample(example);
    }

    @Override
    public  void addUsersUser(UsersUser usersUser){
        usersUserMapper.insert(usersUser);
    }

}
