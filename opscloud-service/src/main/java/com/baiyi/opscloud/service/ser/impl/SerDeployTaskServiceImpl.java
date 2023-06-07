package com.baiyi.opscloud.service.ser.impl;

import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTask;
import com.baiyi.opscloud.mapper.SerDeployTaskMapper;
import com.baiyi.opscloud.service.ser.SerDeployTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author 修远
 * @Date 2023/6/7 10:58 AM
 * @Since 1.0
 */
@Service
@RequiredArgsConstructor
public class SerDeployTaskServiceImpl implements SerDeployTaskService {

    private final SerDeployTaskMapper serDeployTaskMapper;

    @Override
    public void add(SerDeployTask serDeployTask) {
        serDeployTaskMapper.insert(serDeployTask);
    }

    @Override
    public void update(SerDeployTask serDeployTask) {
        serDeployTaskMapper.updateByPrimaryKey(serDeployTask);
    }
}
