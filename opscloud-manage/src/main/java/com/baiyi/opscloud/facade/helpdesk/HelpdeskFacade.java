package com.baiyi.opscloud.facade.helpdesk;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.helpdesk.HelpdeskReportParam;
import com.baiyi.opscloud.domain.vo.helpdesk.HelpdeskReportVO;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/4 3:22 下午
 * @Since 1.0
 */
public interface HelpdeskFacade {

    DataTable<HelpdeskReportVO.HelpdeskReport> helpdeskReportPage(HelpdeskReportParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> saveHelpdeskReport(HelpdeskReportParam.SaveHelpdeskReport param);

    BusinessWrapper<Boolean> delHelpdeskReport(int id);
}
