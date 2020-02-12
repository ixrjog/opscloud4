package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.mapper.OcAuthGroupMapper;
import com.baiyi.opscloud.service.auth.OcAuthGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/11 2:08 下午
 * @Version 1.0
 */
@Service
public class OcAuthGroupServiceImpl implements OcAuthGroupService {

    @Resource
    private OcAuthGroupMapper ocAuthGroupMapper;

}
