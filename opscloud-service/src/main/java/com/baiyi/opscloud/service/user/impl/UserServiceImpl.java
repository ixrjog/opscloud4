package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.annotation.Encrypt;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.mapper.opscloud.UserMapper;
import com.baiyi.opscloud.service.user.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Author baiyi
 * @Date 2021/5/14 10:27 上午
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public DataTable<User> queryPageByParam(UserParam.UserPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<User> data = userMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public User getById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User getByUsername(String username) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public void add(User user) {
        userMapper.insert(user);
    }

    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    @Encrypt
    public void updateBySelective(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<User> listActive() {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", true);
        return userMapper.selectByExample(example);
    }

    @Override
    public List<User> listInactive() {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isActive", false);
        return userMapper.selectByExample(example);
    }
}
