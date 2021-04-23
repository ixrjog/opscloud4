package com.baiyi.opscloud.facade.dashboard.impl;

import com.baiyi.opscloud.common.base.HelpDeskType;
import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.domain.vo.dashboard.DashboardVO;
import com.baiyi.opscloud.domain.vo.dashboard.HeplDeskGroupByType;
import com.baiyi.opscloud.facade.dashboard.DashboardFacade;
import com.baiyi.opscloud.service.helpdesk.OcHelpdeskReportService;
import com.google.common.collect.Lists;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/11/23 11:29 上午
 * @Version 1.0
 */

@Component("DashboardFacade")
public class DashboardFacadeImpl implements DashboardFacade {

    @Resource
    private OcHelpdeskReportService ocHelpdeskReportService;

    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_DASHBOARD_CACHE_REPO, key = "'helpdeskReport'")
    public DashboardVO.HelpDeskReportGroupByWeeks queryHelpDeskGroupByWeeks() {
        DashboardVO.HelpDeskReportGroupByWeeks charts = new DashboardVO.HelpDeskReportGroupByWeeks();
        charts.setHeplDeskGroupByWeeks(ocHelpdeskReportService.queryHelpdeskGroupByWeek());
        return charts;
    }

    @Override
    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_DASHBOARD_CACHE_REPO, key = "'helpdeskReport'", beforeInvocation = true)
    public void evictHelpdeskReport() {
    }


    @Override
    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_DASHBOARD_CACHE_REPO, key = "'helpdeskTypeReport'")
    public DashboardVO.HelpDeskReportGroupByTypes queryHelpDeskGroupByTypes() {
        DashboardVO.HelpDeskReportGroupByTypes charts = new DashboardVO.HelpDeskReportGroupByTypes();
        // List 转 Map
        Map<Integer, HeplDeskGroupByType> helpDeskTypeMap = ocHelpdeskReportService.queryHelpdeskGroupByType()
                .stream().collect(Collectors.toMap(HeplDeskGroupByType::getHelpdeskType, a -> a, (k1, k2) -> k1));

        List<HeplDeskGroupByType> types = Lists.newArrayList();

        for (HelpDeskType type : HelpDeskType.values()) {
            if (helpDeskTypeMap.containsKey(type.getType())) {
                HeplDeskGroupByType t = helpDeskTypeMap.get(type.getType());
                t.setName(type.getDesc());
                types.add(t);
            } else {
                HeplDeskGroupByType t = new HeplDeskGroupByType();
                t.setHelpdeskType(type.getType());
                t.setValue(0);
                t.setName(type.getDesc());
                types.add(t);
            }
        }
        charts.setHeplDeskGroupByTypes(types);
        return charts;
    }

    @Override
    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_DASHBOARD_CACHE_REPO, key = "'helpdeskTypeReport'", beforeInvocation = true)
    public void evictHelpdeskTypeReport() {
    }
}
