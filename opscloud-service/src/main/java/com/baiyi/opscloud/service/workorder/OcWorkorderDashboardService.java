package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.vo.workorder.WorkorderStatsVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/25 2:37 下午
 * @Since 1.0
 */
public interface OcWorkorderDashboardService {

    List<WorkorderStatsVO.WorkorderMonthStatsData> queryWorkorderStatisticsByMonth(Integer workorderId);

    List<WorkorderStatsVO.BaseStatsData> queryWorkorderStatisticsByName();
}
