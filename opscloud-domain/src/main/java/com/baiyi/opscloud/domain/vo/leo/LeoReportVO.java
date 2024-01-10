package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/1/11 8:20
 * @Version 1.0
 */
public class LeoReportVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class LeoReport implements Serializable {

        @Serial
        private static final long serialVersionUID = 5737296480391365951L;

        @Schema(description = "仪表盘")
        private Dashboard dashboard;

        @Schema(description = "持续交付报表")
        private ReportVO.MonthlyReport continuousDeliveryReport;

        private List<ReportVO.Report> buildWithEnvReport;

        private List<ReportVO.Report> deployWithEnvReport;

        @Schema(description = "Jenkins实例")
        private List<LeoJenkinsInstance> instances;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class LeoProdReport implements Serializable {

        @Serial
        private static final long serialVersionUID = 4328652269389010137L;

        @Schema(description = "持续交付报表")
        private ReportVO.DailyReport continuousDeliveryReport;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Dashboard implements Serializable {

        @Serial
        private static final long serialVersionUID = -2386828955543538401L;

        @Schema(description = "应用总数")
        private Integer applicationTotal;

        @Schema(description = "任务总数")
        private Integer jobTotal;

        @Schema(description = "构建总数")
        private Integer buildTotal;

        @Schema(description = "部署总数")
        private Integer deployTotal;

        @Schema(description = "用户总数<使用过的>")
        private Integer userTotal;

        @Schema(description = "授权用户总数")
        private Integer authorizedUserTotal;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class MonthReport implements Serializable {

        @Serial
        private static final long serialVersionUID = 1759830997901199516L;

        @Schema(description = "日期(月)")
        private List<String> dateCat;

        @Schema(description = "月度统计")
        private List<Integer> values;

        public static MonthReport buildMonthReport(List<ReportVO.Report> reports) {
            List<String> dateCat = Lists.newArrayList();
            List<Integer> values = Lists.newArrayList();
            reports.forEach(e -> {
                dateCat.add(e.getCName());
                values.add(e.getValue());
            });
            return MonthReport.builder()
                    .dateCat(dateCat)
                    .values(values)
                    .build();
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class LeoJenkinsInstance implements Serializable {

        @Serial
        private static final long serialVersionUID = -8703855218611943799L;

        @Schema(description = "数据源实例ID")
        private Integer instanceId;

        @Schema(description = "数据源实例名称")
        private String instanceName;

        @Schema(description = "数据源实例标签")
        private List<TagVO.Tag> tags;

    }

}