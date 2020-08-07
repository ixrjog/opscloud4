package com.baiyi.opscloud.service.serverChange;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/27 1:47 下午
 * @Version 1.0
 */
public interface OcServerChangeTaskService {

    void addOcServerChangeTask(OcServerChangeTask ocServerChangeTask);

    void updateOcServerChangeTask(OcServerChangeTask ocServerChangeTask);

    OcServerChangeTask checkOcServerChangeTask(OcServerChangeTask ocServerChangeTask);

    OcServerChangeTask queryOcServerChangeTaskByTaskId(String taskId);

    List<OcServerChangeTask> queryOcServerChangeTaskByTaskStatus(Integer taskStatus);
}
