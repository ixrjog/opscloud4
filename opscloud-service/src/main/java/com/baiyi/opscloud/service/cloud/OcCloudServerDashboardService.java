package com.baiyi.opscloud.service.cloud;

import com.baiyi.opscloud.domain.param.cloud.CloudServerStatsParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudServerStatsVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/21 2:41 下午
 * @Since 1.0
 */
public interface OcCloudServerDashboardService {

    List<CloudServerStatsVO.ServerMonthStats> queryServerMonthStatsReport(Integer queryYear);

    CloudServerStatsVO.ServerResStats queryServerResStatsReport(Integer cloudServerType);

    List<CloudServerStatsVO.ServerMonthStatsByType> queryServerMonthStatsReportByType(CloudServerStatsParam.MonthStats param);
}
