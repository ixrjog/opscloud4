package com.baiyi.opscloud.service.impl;

import com.baiyi.opscloud.domain.generator.OcServerGroup;
import com.baiyi.opscloud.mapper.OcServerGroupMapper;
import com.baiyi.opscloud.service.OcServerGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/8 10:13 上午
 * @Version 1.0
 */
@Service
public class OcServerGroupServiceImpl implements OcServerGroupService {
    @Resource
    private OcServerGroupMapper ocServerGroupMapper;

    @Override
    public OcServerGroup queryOcServerGroupById(Integer id) {
        return ocServerGroupMapper.selectByPrimaryKey(id);
    }
}
