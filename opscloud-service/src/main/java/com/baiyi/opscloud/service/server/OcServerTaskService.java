package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerTask;

/**
 * @Author baiyi
 * @Date 2020/4/7 9:09 下午
 * @Version 1.0
 */
public interface OcServerTaskService {

    void addOcServerTask(OcServerTask ocServerTask);

    void updateOcServerTask(OcServerTask ocServerTask);

    OcServerTask queryOcServerTaskById(int id);
}
