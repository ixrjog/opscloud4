package com.baiyi.opscloud.service.serverChange.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.mapper.opscloud.OcServerChangeTaskMapper;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/27 1:48 下午
 * @Version 1.0
 */
@Service
public class OcServerChangeTaskServiceImpl implements OcServerChangeTaskService {


    @Resource
    private OcServerChangeTaskMapper ocServerChangeTaskMapper;

    @Override
    public void addOcServerChangeTask(OcServerChangeTask ocServerChangeTask) {
        ocServerChangeTaskMapper.insert(ocServerChangeTask);
    }
}
