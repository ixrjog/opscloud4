package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.WorkEvent;
import com.baiyi.opscloud.domain.param.workevent.WorkEventParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import feign.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

public interface WorkEventMapper extends Mapper<WorkEvent>, InsertListMapper<WorkEvent> {

    List<WorkEvent> queryPageByParam(WorkEventParam.WorkEventPageQuery pageQuery);

    List<ReportVO.Report> queryWeek(Integer workRoleId);

    List<ReportVO.Report> queryWeekByItem(@Param Integer workRoleId, @Param Integer workItemId);

    List<ReportVO.Report> getWorkEventItemReport(Integer workRoleId);

    List<ReportVO.CommonReport> getWorkEventTimeReport();

    List<ReportVO.CommonReport> getWorkEventInterceptReport();

    List<ReportVO.CommonReport> getWorkEventSolveReport();

    List<ReportVO.CommonReport> getWorkEventFaultReport();

}