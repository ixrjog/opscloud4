package com.baiyi.opscloud.service.ser.impl;

import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTaskLog;
import com.baiyi.opscloud.mapper.SerDeployTaskLogMapper;
import com.baiyi.opscloud.service.ser.SerDeployTaskLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author 修远
 * @Date 2023/6/7 10:59 AM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class SerDeployTaskLogServiceImpl implements SerDeployTaskLogService {

    private final SerDeployTaskLogMapper serDeployTaskLogMapper;

    @Override
    public void add(SerDeployTaskLog serDeployTaskLog) {
        serDeployTaskLogMapper.insert(serDeployTaskLog);
    }

    @Override
    public void update(SerDeployTaskLog serDeployTaskLog) {
        serDeployTaskLogMapper.updateByPrimaryKey(serDeployTaskLog);
    }
}
