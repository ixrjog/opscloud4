package com.baiyi.opscloud.service.ser;

import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTask;

/**
 * @Author 修远
 * @Date 2023/6/7 10:57 AM
 * @Since 1.0
 */
public interface SerDeployTaskService {

    void add(SerDeployTask serDeployTask);

    void update(SerDeployTask serDeployTask);
}
