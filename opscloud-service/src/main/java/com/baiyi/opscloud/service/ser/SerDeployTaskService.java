package com.baiyi.opscloud.service.ser;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.SerDeployTask;
import com.baiyi.opscloud.domain.param.ser.SerDeployParam;

/**
 * @Author 修远
 * @Date 2023/6/7 10:57 AM
 * @Since 1.0
 */
public interface SerDeployTaskService {

    void add(SerDeployTask serDeployTask);

    void update(SerDeployTask serDeployTask);

    void updateKeySelective(SerDeployTask serDeployTask);

    SerDeployTask getById(Integer id);

    SerDeployTask getByName(String taskName);

    SerDeployTask getByUuid(String taskUuid);

    DataTable<SerDeployTask> queryPageByParam(SerDeployParam.TaskPageQuery pageQuery);

}