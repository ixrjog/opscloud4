package com.baiyi.opscloud.facade.datasource.report.impl;

import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.param.report.ApolloReportParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.domain.vo.datasource.report.ApolloReportVO;
import com.baiyi.opscloud.facade.datasource.report.ApolloReportFacade;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2023/7/17 16:23
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApolloReportFacadeImpl implements ApolloReportFacade {

    private final DsInstanceAssetService dsInstanceAssetService;

    private final DsInstanceService dsInstanceService;

    @Override
    public ApolloReportVO.ApolloReleaseReport getApolloReleaseReport(ApolloReportParam.ApolloReleaseReport apolloReleaseReport) {
        DatasourceInstance dsInstance = dsInstanceService.getById(apolloReleaseReport.getInstanceId());
        apolloReleaseReport.setInstanceUuid(dsInstance.getUuid());
        apolloReleaseReport.setEnvName("PROD");
        apolloReleaseReport.setGray(Boolean.FALSE.toString());
        List<ReportVO.Report> prodReports = dsInstanceAssetService.statApolloReleaseLast30Days(apolloReleaseReport);
        apolloReleaseReport.setGray(Boolean.TRUE.toString());
        List<ReportVO.Report> grayReports = dsInstanceAssetService.statApolloReleaseLast30Days(apolloReleaseReport);
        ReportVO.DailyReport dailyReport = ReportVO.DailyReport.builder()
                .dateCat(prodReports.stream().map(ReportVO.Report::getCName).collect(Collectors.toList()))
                .build()
                .put("PROD", prodReports);
        dailyReport.put("GRAY", grayReports);
        return ApolloReportVO.ApolloReleaseReport.builder()
                .releaseReport(dailyReport)
                .build();
    }

}
