package com.baiyi.opscloud.facade.datasource.report;

import com.baiyi.opscloud.domain.param.report.ApolloReportParam;
import com.baiyi.opscloud.domain.vo.datasource.report.ApolloReportVO;

/**
 * @Author baiyi
 * @Date 2023/7/17 16:23
 * @Version 1.0
 */
public interface ApolloReportFacade {

    ApolloReportVO.ApolloReleaseReport getApolloReleaseReport(ApolloReportParam.ApolloReleaseReport apolloReleaseReport);

}
