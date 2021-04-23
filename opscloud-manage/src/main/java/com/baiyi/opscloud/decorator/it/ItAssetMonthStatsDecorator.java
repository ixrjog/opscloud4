package com.baiyi.opscloud.decorator.it;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetName;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetType;
import com.baiyi.opscloud.domain.vo.it.ItAssetVO;
import com.baiyi.opscloud.service.it.OcItAssetDashboardService;
import com.baiyi.opscloud.service.it.OcItAssetNameService;
import com.baiyi.opscloud.service.it.OcItAssetTypeService;
import com.google.common.collect.Maps;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/3 4:06 下午
 * @Since 1.0
 */

@Component("ItAssetMonthStatsDecorator")
public class ItAssetMonthStatsDecorator {

    @Resource
    private OcItAssetDashboardService ocItAssetDashboardService;

    @Resource
    private OcItAssetTypeService ocItAssetTypeService;

    @Resource
    private OcItAssetNameService ocItAssetNameService;


    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'ItAssetMonthStatsDecorator'", beforeInvocation = true)
    public void evictPreview() {
    }

    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'ItAssetMonthStatsDecorator'")
    public ItAssetVO.AssetMonthStats queryItAssetMonthStatistics() {
        ItAssetVO.AssetMonthStats monthStats = new ItAssetVO.AssetMonthStats();
        List<ItAssetVO.AssetMonthStatsData> all = ocItAssetDashboardService.queryItAssetStatisticsByMonth();
        List<String> dataCatList = all.stream().map(ItAssetVO.AssetMonthStatsData::getDateCat).collect(Collectors.toList());
        monthStats.setDateCatList(dataCatList);
        List<OcItAssetType> typeList = ocItAssetTypeService.queryOcItAssetTypeAll();
        Map<String, List<Integer>> typeStatistics = queryAssetTypeStats(typeList);
        monthStats.setTypeStatistics(typeStatistics);
        Map<String, Map<String, List<Integer>>> nameStatistics = queryAssetNameStats(typeList);
        monthStats.setNameStatistics(nameStatistics);
        return monthStats;
    }

    private Map<String, List<Integer>> queryAssetTypeStats(List<OcItAssetType> typeList) {
        Map<String, List<Integer>> map = Maps.newHashMapWithExpectedSize(typeList.size());
        typeList.forEach(type -> {
            List<ItAssetVO.AssetMonthStatsData> typeData = ocItAssetDashboardService.queryItAssetTypeStatisticsByMonth(type.getAssetType());
            map.put(type.getAssetType(), typeData.stream().map(ItAssetVO.AssetMonthStatsData::getValue).collect(Collectors.toList()));
        });
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x->map.put(x.getKey(),x.getValue()));
        return map;
    }

    private Map<String, Map<String, List<Integer>>> queryAssetNameStats(List<OcItAssetType> typeList) {
        Map<String, Map<String, List<Integer>>> map =Maps.newHashMapWithExpectedSize(typeList.size());
        typeList.forEach(type -> {
            List<OcItAssetName> nameList = ocItAssetNameService.queryOcItAssetNameByType(type.getId());
            Map<String, List<Integer>> nameMap = Maps.newHashMap();
            nameList.forEach(name -> {
                List<ItAssetVO.AssetMonthStatsData> nameData = ocItAssetDashboardService.queryItAssetNameStatisticsByMonth(name.getAssetName());
                nameMap.put(name.getAssetName(), nameData.stream().map(ItAssetVO.AssetMonthStatsData::getValue).collect(Collectors.toList()));
            });
            map.put(type.getAssetType(),nameMap);
        });
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x->map.put(x.getKey(),x.getValue()));
        return map;
    }
}
