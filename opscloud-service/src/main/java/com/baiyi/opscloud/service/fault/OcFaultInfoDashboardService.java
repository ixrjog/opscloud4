package com.baiyi.opscloud.service.fault;

import com.baiyi.opscloud.domain.vo.fault.FaultVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 4:20 下午
 * @Since 1.0
 */
public interface OcFaultInfoDashboardService {

    List<FaultVO.InfoMonthStatsData> queryFaultStatsMonth(Integer queryYear);
}
