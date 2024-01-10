package com.baiyi.opscloud.domain.vo.datasource.report;

import com.baiyi.opscloud.domain.vo.base.ReportVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2023/7/17 16:29
 * @Version 1.0
 */
public class ApolloReportVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ApolloReleaseReport implements Serializable {

        @Serial
        private static final long serialVersionUID = 3905268497635001618L;

        @Schema(description = "发布报表")
        private ReportVO.DailyReport releaseReport;

    }

}