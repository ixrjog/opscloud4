package com.baiyi.opscloud.service.cloud.impl;

import com.baiyi.opscloud.domain.param.cloud.CloudServerStatsParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudServerStatsVO;
import com.baiyi.opscloud.mapper.opscloud.OcCloudServerDashboardMapper;
import com.baiyi.opscloud.service.cloud.OcCloudServerDashboardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/21 2:41 下午
 * @Since 1.0
 */
@Service
public class OcCloudServerDashboardServiceImpl implements OcCloudServerDashboardService {

    @Resource
    private OcCloudServerDashboardMapper ocCloudServerDashboardMapper;


    @Override
    public List<CloudServerStatsVO.ServerMonthStats> queryServerMonthStatsReport(Integer queryYear) {
        return ocCloudServerDashboardMapper.queryServerMonthStatsReport(queryYear);
    }

    @Override
    public CloudServerStatsVO.ServerResStats queryServerResStatsReport(Integer cloudServerType) {
        return ocCloudServerDashboardMapper.queryServerResStatsReport(cloudServerType);
    }

    @Override
    public List<CloudServerStatsVO.ServerMonthStatsByType> queryServerMonthStatsReportByType(CloudServerStatsParam.MonthStats param) {
        return ocCloudServerDashboardMapper.queryServerMonthStatsReportByType(param.getCloudServerType(), param.getQueryYear());
    }
}
