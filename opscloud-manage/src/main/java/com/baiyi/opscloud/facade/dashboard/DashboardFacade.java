package com.baiyi.opscloud.facade.dashboard;

import com.baiyi.opscloud.domain.vo.dashboard.DashboardVO;

/**
 * @Author baiyi
 * @Date 2020/11/23 11:29 上午
 * @Version 1.0
 */
public interface DashboardFacade {

    DashboardVO.HelpDeskReportGroupByWeeks queryHelpDeskGroupByWeeks();

    void evictHelpdeskReport();

    DashboardVO.HelpDeskReportGroupByTypes queryHelpDeskGroupByTypes();

    void evictHelpdeskTypeReport();
}
