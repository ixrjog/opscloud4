package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.server.ServerChangeParam;
import com.baiyi.opscloud.domain.vo.serverChange.ServerChangeTaskVO;

/**
 * @Author baiyi
 * @Date 2020/5/26 4:43 下午
 * @Version 1.0
 */
public interface ServerChangeFacade {

   BusinessWrapper<Boolean> executeServerChangeOffline(ServerChangeParam.ExecuteServerChangeParam executeServerChangeParam);

   ServerChangeTaskVO.ServerChangeTask queryServerChangeTask(String taskId);
}
