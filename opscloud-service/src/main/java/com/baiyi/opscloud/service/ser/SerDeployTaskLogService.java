package com.baiyi.opscloud.service.ser;

import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTaskLog;

/**
 * @Author 修远
 * @Date 2023/6/7 10:58 AM
 * @Since 1.0
 */
public interface SerDeployTaskLogService {

    void add(SerDeployTaskLog serDeployTaskLog);

    void update(SerDeployTaskLog serDeployTaskLog);
}
