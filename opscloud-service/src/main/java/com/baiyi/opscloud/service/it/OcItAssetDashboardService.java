package com.baiyi.opscloud.service.it;

import com.baiyi.opscloud.domain.vo.it.ItAssetVO;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/2 5:16 下午
 * @Since 1.0
 */
public interface OcItAssetDashboardService {

    List<ItAssetVO.StatsData> queryItAssetNameStatistics();

    List<ItAssetVO.StatsData> queryItAssetTypeStatistics();

    List<ItAssetVO.AssetMonthStatsData> queryItAssetNameStatisticsByMonth(String name);

    List<ItAssetVO.AssetMonthStatsData> queryItAssetTypeStatisticsByMonth(String name);

    List<ItAssetVO.AssetMonthStatsData> queryItAssetStatisticsByMonth();

    List<ItAssetVO.AssetCompanyStats> queryAssetCompanyStats();


}
