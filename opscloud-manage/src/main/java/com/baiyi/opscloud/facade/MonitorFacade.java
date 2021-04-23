package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.monitor.MonitorHostParam;
import com.baiyi.opscloud.domain.param.monitor.MonitorUserParam;
import com.baiyi.opscloud.domain.vo.monitor.MonitorVO;

/**
 * @Author baiyi
 * @Date 2020/11/24 10:38 上午
 * @Version 1.0
 */
public interface MonitorFacade {

    DataTable<MonitorVO.Host> queryMonitorHostPage(MonitorHostParam.MonitorHostPageQuery pageQuery);

    BusinessWrapper<Boolean>  massUpdateMonitorHostStatus(MonitorHostParam.MassUpdateMonitorHostStatus massUpdateMonitorHostStatus);

    /**
     * 同步监控主机信息
     */
    void syncMonitorHostStatus();

    BusinessWrapper<Boolean> createMonitorHost(MonitorHostParam.CreateMonitorHost createMonitorHost);

    BusinessWrapper<Boolean> setMonitorHostStatus(int id);

    BusinessWrapper<Boolean> pushMonitorHost(MonitorHostParam.PushMonitorHost pushMonitorHost);

    DataTable<MonitorVO.User> queryMonitorUserPage(MonitorUserParam.MonitorUserPageQuery pageQuery);

    void syncZabbixUsers();
}
