package com.baiyi.opscloud.service.ser;

import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTaskItem;

import java.util.List;

/**
 * @Author 修远
 * @Date 2023/6/7 10:58 AM
 * @Since 1.0
 */
public interface SerDeployTaskItemService {

    void add(SerDeployTaskItem serDeployTaskItem);

    void update(SerDeployTaskItem serDeployTaskItem);

    SerDeployTaskItem getById(Integer id);

    void deleteById(Integer id);

    List<SerDeployTaskItem> listBySerDeployTaskId(Integer serDeployTaskId);

    SerDeployTaskItem getByTaskIdAndItemName(Integer serDeployTaskId, String itemName);

}