package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;
import com.baiyi.opscloud.domain.vo.server.OcServerTaskVO;

/**
 * @Author baiyi
 * @Date 2020/4/7 6:36 下午
 * @Version 1.0
 */
public interface ServerTaskFacade {

   BusinessWrapper<Boolean> executorCommand(ServerTaskExecutorParam.ServerTaskCommandExecutor serverTaskCommandExecutor);

   OcServerTaskVO.ServerTask queryServerTaskByTaskId(int taskId);
}
