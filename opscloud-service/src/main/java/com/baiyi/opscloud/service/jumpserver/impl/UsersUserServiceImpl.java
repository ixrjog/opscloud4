package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.domain.param.jumpserver.user.UsersUserPageParam;
import com.baiyi.opscloud.mapper.jumpserver.UsersUserMapper;
import com.baiyi.opscloud.service.jumpserver.UsersUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

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
    public DataTable<UsersUser> fuzzyQueryUsersUserPage(UsersUserPageParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<UsersUser> usersUserList = usersUserMapper.fuzzyQueryUsersUserPage(pageQuery);
        return new DataTable<>(usersUserList, page.getTotal());
    }

    @Override
    public DataTable<UsersUser> fuzzyQueryAdminUsersUserPage(UsersUserPageParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<UsersUser> usersUserList = usersUserMapper.fuzzyQueryAdminUsersUserPage(pageQuery);
        return new DataTable<>(usersUserList, page.getTotal());
    }

    @Override
    public UsersUser queryUsersUserByUsername(String username) {
        Example example = new Example(UsersUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return usersUserMapper.selectOneByExample(example);
    }

    @Override
    public  UsersUser queryUsersUserById(String id){
        return usersUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public  UsersUser queryUsersUserByEmail(String email){
        Example example = new Example(UsersUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("email", email);
        return usersUserMapper.selectOneByExample(example);
    }

    @Override
    public void addUsersUser(UsersUser usersUser) {
        usersUserMapper.insert(usersUser);
    }

    @Override
    public void updateUsersUser(UsersUser usersUser) {
        usersUserMapper.updateByPrimaryKey(usersUser);
    }

    @Override
    public void delUsersUserById(String id) {
        usersUserMapper.deleteByPrimaryKey(id);
    }

}
