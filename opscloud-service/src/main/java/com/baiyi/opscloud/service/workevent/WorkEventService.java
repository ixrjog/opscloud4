package com.baiyi.opscloud.service.workevent;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.WorkEvent;
import com.baiyi.opscloud.domain.param.workevent.WorkEventParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/8/5 4:21 PM
 * @Since 1.0
 */
public interface WorkEventService {

    void update(WorkEvent workEvent);

    void addList(List<WorkEvent> workEventList);

    void add(WorkEvent workEvent);

    void deleteById(Integer id);

    WorkEvent getById(Integer id);

    DataTable<WorkEvent> queryPageByParam(WorkEventParam.WorkEventPageQuery pageQuery);

    List<ReportVO.Report> queryWeek(Integer workRoleId);

    List<ReportVO.Report> queryWeekByItem(Integer workRoleId, Integer workItemId);

    List<ReportVO.Report> getWorkEventItemReport(Integer workRoleId);

    List<ReportVO.CommonReport> getWorkEventTimeReport();

    List<ReportVO.CommonReport> getWorkEventInterceptReport();

    List<ReportVO.CommonReport> getWorkEventSolveReport();

    List<ReportVO.CommonReport> getWorkEventFaultReport();

}