package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.vo.fault.FaultVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 5:11 下午
 * @Since 1.0
 */
public interface OcFaultInfoDashboardMapper {

    List<FaultVO.InfoMonthStatsData> queryFaultStatsMonth(Integer queryYear);
}
