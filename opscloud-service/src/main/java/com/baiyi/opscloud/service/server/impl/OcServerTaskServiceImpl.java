package com.baiyi.opscloud.service.server.impl;

import com.baiyi.opscloud.domain.generator.OcServerTask;
import com.baiyi.opscloud.mapper.opscloud.OcServerTaskMapper;
import com.baiyi.opscloud.service.server.OcServerTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/4/7 9:09 下午
 * @Version 1.0
 */
@Service
public class OcServerTaskServiceImpl implements OcServerTaskService {

    @Resource
    private OcServerTaskMapper ocServerTaskMapper;

    @Override
    public void addOcServerTask(OcServerTask ocServerTask) {
        ocServerTaskMapper.insert(ocServerTask);
    }

    @Override
    public void updateOcServerTask(OcServerTask ocServerTask) {
        ocServerTaskMapper.updateByPrimaryKey(ocServerTask);
    }

    @Override
    public OcServerTask queryOcServerTaskById(int id) {
        return ocServerTaskMapper.selectByPrimaryKey(id);
    }

}
