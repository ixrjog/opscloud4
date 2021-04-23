package com.baiyi.opscloud.service.it.impl;

import com.baiyi.opscloud.domain.vo.it.ItAssetVO;
import com.baiyi.opscloud.mapper.opscloud.OcItAssetDashboardMapper;
import com.baiyi.opscloud.service.it.OcItAssetDashboardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/2 5:16 下午
 * @Since 1.0
 */

@Service
public class OcItAssetDashboardServiceImpl implements OcItAssetDashboardService {

    @Resource
    private OcItAssetDashboardMapper ocItAssetDashboardMapper;

    @Override
    public List<ItAssetVO.StatsData> queryItAssetNameStatistics() {
        return ocItAssetDashboardMapper.queryItAssetNameStatistics();
    }

    @Override
    public List<ItAssetVO.StatsData> queryItAssetTypeStatistics() {
        return ocItAssetDashboardMapper.queryItAssetTypeStatistics();
    }

    @Override
    public List<ItAssetVO.AssetMonthStatsData> queryItAssetNameStatisticsByMonth(String name) {
        return ocItAssetDashboardMapper.queryItAssetNameStatisticsByMonth(name);
    }

    @Override
    public List<ItAssetVO.AssetMonthStatsData> queryItAssetTypeStatisticsByMonth(String name) {
        return ocItAssetDashboardMapper.queryItAssetTypeStatisticsByMonth(name);
    }

    @Override
    public List<ItAssetVO.AssetMonthStatsData> queryItAssetStatisticsByMonth() {
        return ocItAssetDashboardMapper.queryItAssetStatisticsByMonth();
    }

    @Override
    public List<ItAssetVO.AssetCompanyStats> queryAssetCompanyStats() {
        return ocItAssetDashboardMapper.queryAssetCompanyStats();
    }
}
