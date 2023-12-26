package com.baiyi.opscloud.service.ser;

import com.baiyi.opscloud.domain.generator.opscloud.SerDeploySubtask;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/6/7 10:58 AM
 * @Since 1.0
 */
public interface SerDeploySubtaskService {

    void add(SerDeploySubtask serDeploySubtask);

    void update(SerDeploySubtask serDeploySubtask);

    SerDeploySubtask getById(Integer id);

    List<SerDeploySubtask> listBySerDeployTaskId(Integer serDeployTaskId);

    SerDeploySubtask getByTaskIdAndEnvType(Integer serDeployTaskId, Integer envType);

}