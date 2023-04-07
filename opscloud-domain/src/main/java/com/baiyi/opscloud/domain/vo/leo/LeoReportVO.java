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

        @Schema(name = "仪表盘")
        private Dashboard dashboard;

        @Schema(name = "持续交付报表")
        private ReportVO.MonthlyReport continuousDeliveryReport;

        private List<ReportVO.Report> buildWithEnvReport;

        private List<ReportVO.Report> deployWithEnvReport;

//        @Schema(name = "构建月报表")
//        private MonthReport buildMonthReport;
//
//        @Schema(name = "部署月报表")
//        private MonthReport deployMonthReport;

        @Schema(name = "Jenkins实例")
        private List<LeoJenkinsInstance> instances;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Dashboard implements Serializable {

        @Serial
        private static final long serialVersionUID = -2386828955543538401L;

        @Schema(name = "应用总数")
        private Integer applicationTotal;

        @Schema(name = "任务总数")
        private Integer jobTotal;

        @Schema(name = "构建总数")
        private Integer buildTotal;

        @Schema(name = "部署总数")
        private Integer deployTotal;

        @Schema(name = "用户总数<使用过的>")
        private Integer userTotal;

        @Schema(name = "授权用户总数")
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

        @Schema(name = "日期(月)")
        private List<String> dateCat;

        @Schema(name = "月度统计")
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

        @Schema(name = "数据源实例ID")
        private Integer instanceId;

        @Schema(name = "数据源实例名称")
        private String instanceName;

        @Schema(name = "数据源实例标签")
        private List<TagVO.Tag> tags;

    }

}
