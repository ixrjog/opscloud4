package com.baiyi.opscloud.service.helpdesk;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcHelpdeskReport;
import com.baiyi.opscloud.domain.param.helpdesk.HelpdeskReportParam;
import com.baiyi.opscloud.domain.vo.dashboard.HeplDeskGroupByType;
import com.baiyi.opscloud.domain.vo.dashboard.HeplDeskGroupByWeek;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/11/23 11:24 上午
 * @Version 1.0
 */
public interface OcHelpdeskReportService {

    List<HeplDeskGroupByWeek> queryHelpdeskGroupByWeek();

    List<HeplDeskGroupByType> queryHelpdeskGroupByType();

    DataTable<OcHelpdeskReport> OcHelpdeskReportPage(HelpdeskReportParam.PageQuery pageQuery);

    void addOcHelpdeskReportList(List<OcHelpdeskReport> ocHelpdeskReportList);

    void deleteHelpdeskReport(int id);

}
