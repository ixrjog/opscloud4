package com.baiyi.caesar.service.user.impl;

import com.baiyi.caesar.domain.generator.caesar.UserPermission;
import com.baiyi.caesar.mapper.caesar.UserPermissionMapper;
import com.baiyi.caesar.service.user.UserPermissionService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/26 5:39 下午
 * @Version 1.0
 */
@Service
public class UserPermissionServiceImpl implements UserPermissionService {

    @Resource
    private UserPermissionMapper permissionMapper;

    @Override
    public UserPermission getById(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(UserPermission userPermission) {
        permissionMapper.updateByPrimaryKey(userPermission);
    }

    @Override
    public void add(UserPermission userPermission) {
        permissionMapper.insert(userPermission);
    }

    @Override
    public void deleteByUserPermission(UserPermission userPermission) {
        permissionMapper.delete(userPermission);
    }

    @Override
    public UserPermission getByUserPermission(UserPermission userPermission) {
        return permissionMapper.selectOne(userPermission);
    }

    @Override
    public int countByBusiness(UserPermission userPermission) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", userPermission.getBusinessType())
                .andEqualTo("businessId", userPermission.getBusinessId());

        return permissionMapper.selectCountByExample(example);
    }

    @Override
    public List<UserPermission> queryByBusiness(UserPermission userPermission) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", userPermission.getBusinessType())
                .andEqualTo("businessId", userPermission.getBusinessId());

        return permissionMapper.selectByExample(example);
    }

}
