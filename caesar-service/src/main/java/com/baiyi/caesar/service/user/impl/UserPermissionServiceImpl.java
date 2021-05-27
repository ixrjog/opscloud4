package com.baiyi.caesar.service.user.impl;

import com.baiyi.caesar.domain.generator.caesar.UserPermission;
import com.baiyi.caesar.mapper.caesar.UserPermissionMapper;
import com.baiyi.caesar.service.user.UserPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    public void add(UserPermission userPermission) {
        permissionMapper.insert(userPermission);
    }
}
