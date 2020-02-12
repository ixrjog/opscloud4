package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.mapper.OcAuthUserRoleMapper;
import com.baiyi.opscloud.service.auth.OcAuthUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/12 3:21 下午
 * @Version 1.0
 */
@Service
public class OcAuthUserRoleServiceImpl implements OcAuthUserRoleService {

    @Resource
    private OcAuthUserRoleMapper ocAuthUserRoleMapper;
}
