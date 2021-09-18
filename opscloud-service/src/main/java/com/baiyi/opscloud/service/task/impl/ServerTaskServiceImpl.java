package com.baiyi.opscloud.service.task.impl;

import com.baiyi.opscloud.domain.generator.opscloud.ServerTask;
import com.baiyi.opscloud.mapper.opscloud.ServerTaskMapper;
import com.baiyi.opscloud.service.task.ServerTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/9/18 5:19 下午
 * @Version 1.0
 */
@Service
public class ServerTaskServiceImpl implements ServerTaskService {

    @Resource
    private ServerTaskMapper serverTaskMapper;

    @Override
    public void add(ServerTask serverTask) {
        serverTaskMapper.insert(serverTask);
    }

    @Override
    public void update(ServerTask serverTask) {
        serverTaskMapper.updateByPrimaryKey(serverTask);
    }
}
