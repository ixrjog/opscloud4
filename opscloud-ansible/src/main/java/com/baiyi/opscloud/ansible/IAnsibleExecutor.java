package com.baiyi.opscloud.ansible;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.server.ServerTaskExecutorParam;

/**
 * @Author baiyi
 * @Date 2020/4/17 8:57 上午
 * @Version 1.0
 */
public interface IAnsibleExecutor {

    String getKey();

    BusinessWrapper<Boolean> executorByParam(ServerTaskExecutorParam.TaskExecutor taskExecutor);
}
