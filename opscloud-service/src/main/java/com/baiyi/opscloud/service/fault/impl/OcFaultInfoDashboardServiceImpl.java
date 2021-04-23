package com.baiyi.opscloud.service.fault.impl;

import com.baiyi.opscloud.domain.vo.fault.FaultVO;
import com.baiyi.opscloud.mapper.opscloud.OcFaultInfoDashboardMapper;
import com.baiyi.opscloud.service.fault.OcFaultInfoDashboardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 4:20 下午
 * @Since 1.0
 */

@Service
public class OcFaultInfoDashboardServiceImpl implements OcFaultInfoDashboardService {

    @Resource
    private OcFaultInfoDashboardMapper ocFaultInfoDashboardMapper;

    @Override
    public List<FaultVO.InfoMonthStatsData> queryFaultStatsMonth(Integer queryYear) {
        return ocFaultInfoDashboardMapper.queryFaultStatsMonth(queryYear);
    }
}
