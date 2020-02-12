package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.mapper.OcAuthRoleResourceMapper;
import com.baiyi.opscloud.service.auth.OcAuthRoleResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:13 下午
 * @Version 1.0
 */
@Service
public class OcAuthRoleResourceServiceImpl implements OcAuthRoleResourceService {

    @Resource
    private OcAuthRoleResourceMapper ocAuthRoleResourceMapper;

}
