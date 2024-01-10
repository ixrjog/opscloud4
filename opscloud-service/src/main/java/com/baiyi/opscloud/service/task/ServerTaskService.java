package com.baiyi.opscloud.service.task;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.ServerTask;
import com.baiyi.opscloud.domain.param.task.ServerTaskParam;

/**
 * @Author baiyi
 * @Date 2021/9/18 5:18 下午
 * @Version 1.0
 */
public interface ServerTaskService {

    DataTable<ServerTask> queryServerTaskPage(ServerTaskParam.ServerTaskPageQuery pageQuery);

    void add(ServerTask serverTask);

    void update(ServerTask serverTask);

}