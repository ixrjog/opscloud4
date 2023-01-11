package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.domain.vo.leo.LeoReportVO;
import com.baiyi.opscloud.facade.leo.LeoReportFacade;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2023/1/11 18:26
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeoReportFacadeImpl implements LeoReportFacade {

    private final LeoBuildService leoBuildService;

    private final LeoDeployService leoDeployService;

    @Override
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'opscloud.v4.report#statLeoReport'")
    public LeoReportVO.LeoReport statLeoReport() {
        return LeoReportVO.LeoReport.builder()
                .buildMonthReport(
                        LeoReportVO.MonthReport
                                .buildMonthReport(leoBuildService.statByMonth())
                )
                .deployMonthReport(
                        LeoReportVO.MonthReport
                                .buildMonthReport(leoDeployService.statByMonth())
                )
                .build();
    }

}
