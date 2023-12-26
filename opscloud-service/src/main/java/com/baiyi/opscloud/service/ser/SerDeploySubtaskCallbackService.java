package com.baiyi.opscloud.service.ser;

import com.baiyi.opscloud.domain.generator.opscloud.SerDeploySubtaskCallback;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/7/5 7:34 PM
 * @Since 1.0
 */
public interface SerDeploySubtaskCallbackService {

    void add(SerDeploySubtaskCallback serDeploySubtaskCallback);

    List<SerDeploySubtaskCallback> listBySerDeploySubtaskId(Integer serDeploySubtaskId);

    void deleteByBySerDeploySubtaskId(Integer serDeploySubtaskId);

}