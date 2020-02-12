package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.mapper.OcAuthRoleMapper;
import com.baiyi.opscloud.service.auth.OcAuthRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:12 下午
 * @Version 1.0
 */
@Service
public class OcAuthRoleServiceImpl implements OcAuthRoleService {

    @Resource
    private OcAuthRoleMapper ocAuthRoleMapper;

}
