package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.vo.leo.LeoReportVO;
import com.baiyi.opscloud.facade.leo.LeoReportFacade;
import com.baiyi.opscloud.facade.tag.SimpleTagFacade;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    private final InstanceHelper instanceHelper;

    private final SimpleTagFacade simpleTagFacade;

    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.JENKINS};

    @Override
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'opscloud.v4.report#statLeoReport'")
    public LeoReportVO.LeoReport statLeoReport() {
        LeoReportVO.MonthReport buildMonthReport = LeoReportVO.MonthReport
                .buildMonthReport(leoBuildService.statByMonth());
        LeoReportVO.MonthReport deployMonthReport = LeoReportVO.MonthReport
                .buildMonthReport(leoDeployService.statByMonth());
        return LeoReportVO.LeoReport.builder()
                .instances(buildInstances())
                .buildMonthReport(buildMonthReport)
                .deployMonthReport(deployMonthReport)
                .build();
    }

    private List<LeoReportVO.LeoJenkinsInstance> buildInstances() {
        List<DatasourceInstance> dsInstances = instanceHelper.listInstance(FILTER_INSTANCE_TYPES, TagConstants.LEO.getTag());
        if (CollectionUtils.isEmpty(dsInstances)) {
            return Lists.newArrayList();
        }
        return dsInstances.stream().map(e -> {
                    SimpleBusiness queryParam = SimpleBusiness.builder()
                            .businessType(BusinessTypeEnum.DATASOURCE_INSTANCE.getType())
                            .businessId(e.getId())
                            .build();
                    return LeoReportVO.LeoJenkinsInstance.builder()
                            .instanceId(e.getId())
                            .instanceName(e.getInstanceName())
                            .tags(simpleTagFacade.queryTagByBusiness(queryParam))
                            .build();
                }
        ).collect(Collectors.toList());
    }

}
