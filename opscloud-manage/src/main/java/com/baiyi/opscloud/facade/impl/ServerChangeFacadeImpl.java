package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.server.ServerChangeParam;
import com.baiyi.opscloud.facade.ServerChangeFacade;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2020/5/26 4:43 下午
 * @Version 1.0
 */
@Service
public class ServerChangeFacadeImpl implements ServerChangeFacade {

    /**
     * 服务器变更管理
     */

    @Override
    public BusinessWrapper<Boolean> executeServerChangeOffline(ServerChangeParam.ExecuteServerChangeParam executeServerChangeParam) {

        return BusinessWrapper.SUCCESS;
    }


}
