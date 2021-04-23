package com.baiyi.opscloud.facade;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.param.monitor.MonitorHostParam;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/12/10 3:23 下午
 * @Version 1.0
 */
class MonitorFacadeTest extends BaseUnit {

    @Resource
    private MonitorFacade monitorFacade;

    @Test
    void testMassUpdateMonitorHostStatus() {
        MonitorHostParam.MassUpdateMonitorHostStatus param = new MonitorHostParam.MassUpdateMonitorHostStatus();
        param.setHostPattern("account-prod");
        param.setServerGroupName("group_account");
        param.setStatus(0);
        BusinessWrapper<Boolean> wrapper = monitorFacade.massUpdateMonitorHostStatus(param);
        System.err.println(JSON.toJSON(wrapper));
    }

}