package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.UsersUserGroups;
import com.baiyi.opscloud.mapper.jumpserver.UsersUserGroupsMapper;
import com.baiyi.opscloud.service.jumpserver.UsersUserGroupsService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/8 5:25 下午
 * @Version 1.0
 */
@Service
public class UsersUserGroupsServiceImpl implements UsersUserGroupsService {

    @Resource
    private UsersUserGroupsMapper usersUserGroupsMapper;

    @Override
    public UsersUserGroups queryUsersUserGroupsByUniqueKey(UsersUserGroups usersUserGroups) {
        Example example = new Example(UsersUserGroups.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", usersUserGroups.getUserId());
        criteria.andEqualTo("usergroupId", usersUserGroups.getUsergroupId());
        return usersUserGroupsMapper.selectOneByExample(example);
    }

    @Override
    public void addUsersUserGroups(UsersUserGroups usersUserGroups) {
        usersUserGroupsMapper.insert(usersUserGroups);
    }

}
