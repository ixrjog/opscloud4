package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.UsersUsergroup;
import com.baiyi.opscloud.mapper.jumpserver.UsersUsergroupMapper;
import com.baiyi.opscloud.service.jumpserver.UsersUsergroupService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/8 4:57 下午
 * @Version 1.0
 */
@Service
public class UsersUsergroupServiceImpl implements UsersUsergroupService {

    @Resource
    private UsersUsergroupMapper usersUsergroupMapper;

    @Override
    public UsersUsergroup queryUsersUsergroupByName(String name) {
        Example example = new Example(UsersUsergroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return usersUsergroupMapper.selectOneByExample(example);
    }

    /**
     * 查询用户的用户组
     *
     * @param username
     * @return
     */
    @Override
    public List<UsersUsergroup> queryUsersUsergroupByUsername(String username) {
       return usersUsergroupMapper.queryUsersUsergroupByUsername(username);
    }


    @Override
    public void addUsersUsergroup(UsersUsergroup usersUsergroup) {
        usersUsergroupMapper.insert(usersUsergroup);
    }

}
