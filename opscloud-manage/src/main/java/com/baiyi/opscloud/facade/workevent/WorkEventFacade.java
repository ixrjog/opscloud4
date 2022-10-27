package com.baiyi.opscloud.facade.workevent;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.WorkItem;
import com.baiyi.opscloud.domain.generator.opscloud.WorkRole;
import com.baiyi.opscloud.domain.param.workevent.WorkEventParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.domain.vo.common.TreeVO;
import com.baiyi.opscloud.domain.vo.workevent.WorkEventReportVO;
import com.baiyi.opscloud.domain.vo.workevent.WorkEventVO;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/8/12 10:23 AM
 * @Since 1.0
 */
public interface WorkEventFacade {

    DataTable<WorkEventVO.WorkEvent> queryWorkEventPage(WorkEventParam.WorkEventPageQuery pageQuery);

    void addWorkEvent(WorkEventParam.AddWorkEvent param);

    void updateWorkEvent(WorkEventParam.UpdateWorkEvent param);

    void deleteWorkEvent(Integer id);

    List<WorkRole> queryWorkRole();

    List<WorkRole> queryMyWorkRole();

    WorkRole getWorkRoleById(Integer workRoleId);

    List<WorkItem> listWorkItem(WorkEventParam.WorkItemQuery query);

    List<TreeVO.Tree> queryWorkItemTree(WorkEventParam.WorkItemTreeQuery param);

    WorkEventVO.Item getWorkItemById(Integer workItemId);

    WorkEventReportVO.WeeklyReport getWorkEventWeeklyReport(Integer workRoleId);

    List<WorkEventReportVO.ItemReport> getWorkEventItemReport(Integer workRoleId);

    List<ReportVO.CommonReport> getWorkEventTimeReport();

    List<ReportVO.CommonReport> getWorkEventInterceptReport();

    List<ReportVO.CommonReport> getWorkEventSolveReport();

    List<ReportVO.CommonReport> getWorkEventFaultReport();

}
