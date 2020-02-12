package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.mapper.OcAuthResourceGroupMapper;
import com.baiyi.opscloud.service.auth.OcAuthResourceGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:09 下午
 * @Version 1.0
 */
@Service
public class OcAuthResourceGroupServiceImpl implements OcAuthResourceGroupService {

    @Resource
    private OcAuthResourceGroupMapper ocAuthResourceGroupMapper;

}
