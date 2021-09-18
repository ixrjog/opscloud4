package com.baiyi.opscloud.facade.task;

import com.baiyi.opscloud.domain.param.task.ServerTaskParam;
import com.baiyi.opscloud.domain.vo.task.ServerTaskVO;

/**
 * @Author baiyi
 * @Date 2021/9/18 3:21 下午
 * @Version 1.0
 */
public interface ServerTaskFacade {

    ServerTaskVO.ServerTask submitServerTask(ServerTaskParam.SubmitServerTask submitServerTask);
}
