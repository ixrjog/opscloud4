package com.baiyi.opscloud.service.workorder.impl;

import com.baiyi.opscloud.domain.vo.workorder.WorkorderStatsVO;
import com.baiyi.opscloud.mapper.opscloud.OcWorkorderDashboardMapper;
import com.baiyi.opscloud.service.workorder.OcWorkorderDashboardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/25 2:37 下午
 * @Since 1.0
 */

@Service
public class OcWorkorderDashboardServiceImpl implements OcWorkorderDashboardService {

    @Resource
    private OcWorkorderDashboardMapper ocWorkorderDashboardMapper;

    @Override
    public List<WorkorderStatsVO.WorkorderMonthStatsData> queryWorkorderStatisticsByMonth(Integer workorderId) {
        return ocWorkorderDashboardMapper.queryWorkorderStatisticsByMonth(workorderId);
    }

    @Override
    public List<WorkorderStatsVO.BaseStatsData> queryWorkorderStatisticsByName() {
        return ocWorkorderDashboardMapper.queryWorkorderStatisticsByName();
    }
}
