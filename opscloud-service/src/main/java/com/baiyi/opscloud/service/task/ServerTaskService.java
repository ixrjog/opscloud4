package com.baiyi.opscloud.service.task;

import com.baiyi.opscloud.domain.generator.opscloud.ServerTask;

/**
 * @Author baiyi
 * @Date 2021/9/18 5:18 下午
 * @Version 1.0
 */
public interface ServerTaskService {

    void add(ServerTask serverTask);

    void update(ServerTask serverTask);
}
