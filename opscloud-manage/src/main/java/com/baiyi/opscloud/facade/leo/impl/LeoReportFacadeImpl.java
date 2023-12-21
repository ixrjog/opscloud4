package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.domain.vo.leo.LeoReportVO;
import com.baiyi.opscloud.facade.leo.LeoReportFacade;
import com.baiyi.opscloud.facade.tag.SimpleTagFacade;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoDeployService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.user.UserPermissionService;
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

    private final LeoBuildService buildService;

    private final LeoDeployService deployService;

    private final ApplicationService applicationService;

    private final LeoJobService jobService;

    private final InstanceHelper instanceHelper;

    private final SimpleTagFacade simpleTagFacade;

    private final UserPermissionService userPermissionService;

    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.JENKINS};

    @Override
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'opscloud.v4.report#statLeoReport'")
    public LeoReportVO.LeoReport statLeoReport() {
        ReportVO.MonthlyReport monthlyReport = ReportVO.MonthlyReport.builder()
                .dateCat(buildService.queryMonth().stream().map(ReportVO.Report::getCName).collect(Collectors.toList()))
                .build()
                .put("BUILD", buildService.statByMonth())
                .put("DEPLOY", deployService.statByMonth());
        return LeoReportVO.LeoReport.builder()
                .dashboard(buildDashboard())
                .instances(buildInstances())
                .continuousDeliveryReport(monthlyReport)
                .buildWithEnvReport(buildService.statByEnvName())
                .deployWithEnvReport(deployService.statByEnvName())
                .build();
    }

    private LeoReportVO.Dashboard buildDashboard() {
        return LeoReportVO.Dashboard.builder()
                .applicationTotal(applicationService.countWithReport())
                .jobTotal(jobService.countWithReport())
                .buildTotal(buildService.countWithReport())
                .deployTotal(deployService.countWithReport())
                .userTotal(buildService.statUserTotal())
                .authorizedUserTotal(userPermissionService.statTotal(BusinessTypeEnum.APPLICATION.getType()))
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

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_10M, key = "'opscloud.v4.report#statLeoProdReport'")
    @Override
    public LeoReportVO.LeoProdReport statLeoProdReport() {
        List<ReportVO.Report> reports = deployService.statLast30Days();
        ReportVO.DailyReport dailyReport = ReportVO.DailyReport.builder()
                .dateCat(reports.stream().map(ReportVO.Report::getCName).collect(Collectors.toList()))
                .build()
                .put("DEPLOY", reports);
        return LeoReportVO.LeoProdReport.builder()
                .continuousDeliveryReport(dailyReport)
                .build();
    }

}