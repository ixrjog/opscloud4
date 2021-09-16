package com.baiyi.opscloud.service.user.impl;

import com.baiyi.opscloud.common.annotation.EventPublisher;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.annotation.BusinessType;
import com.baiyi.opscloud.domain.annotation.Encrypt;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.user.UserBusinessPermissionParam;
import com.baiyi.opscloud.domain.param.user.UserParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.types.EventActionTypeEnum;
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
@BusinessType(BusinessTypeEnum.USER)
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
    public DataTable<User> queryPageByParam(UserBusinessPermissionParam.BusinessPermissionUserPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<User> data = userMapper.queryBusinessPermissionUserPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public User getById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> queryAll(){
        return userMapper.selectAll();
    }

    @Override
    public User getByUsername(String username) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return userMapper.selectOneByExample(example);
    }

    @Override
    @Encrypt
    @EventPublisher(eventAction = EventActionTypeEnum.CREATE)
    public void add(User user) {
        userMapper.insert(user);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.UPDATE)
    public void update(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public void updateLogin(User user) {
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    @EventPublisher(eventAction = EventActionTypeEnum.DELETE)
    public void delete(User user) {
        user.setIsActive(false);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    @Encrypt
    @EventPublisher(eventAction = EventActionTypeEnum.UPDATE)
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
