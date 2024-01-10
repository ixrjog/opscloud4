package com.baiyi.opscloud.facade.task;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.task.ServerTaskParam;
import com.baiyi.opscloud.domain.vo.task.ServerTaskVO;

/**
 * @Author baiyi
 * @Date 2021/9/18 3:21 下午
 * @Version 1.0
 */
public interface ServerTaskFacade {

    DataTable<ServerTaskVO.ServerTask> queryServerTaskPage(ServerTaskParam.ServerTaskPageQuery pageQuery);

    /**
     * 提交服务器任务
     * @param submitServerTask
     * @return
     */
    void submitServerTask(ServerTaskParam.SubmitServerTask submitServerTask,String username);

}