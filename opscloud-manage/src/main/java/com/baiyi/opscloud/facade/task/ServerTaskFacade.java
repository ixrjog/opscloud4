package com.baiyi.opscloud.facade.task;

import com.baiyi.opscloud.domain.param.task.ServerTaskParam;

/**
 * @Author baiyi
 * @Date 2021/9/18 3:21 下午
 * @Version 1.0
 */
public interface ServerTaskFacade {

    /**
     * 提交服务器任务
     * @param submitServerTask
     * @return
     */
    void submitServerTask(ServerTaskParam.SubmitServerTask submitServerTask);
}
