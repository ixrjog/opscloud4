package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.vo.cloud.CloudServerStatsVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/21 2:42 下午
 * @Since 1.0
 */
public interface OcCloudServerDashboardMapper {

    List<CloudServerStatsVO.ServerMonthStats> queryServerMonthStatsReport(Integer queryYear);

    CloudServerStatsVO.ServerResStats queryServerResStatsReport(Integer cloudServerType);

    List<CloudServerStatsVO.ServerMonthStatsByType> queryServerMonthStatsReportByType(Integer cloudServerType, Integer queryYear);
}
