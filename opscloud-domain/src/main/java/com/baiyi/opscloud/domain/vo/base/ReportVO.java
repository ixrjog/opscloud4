package com.baiyi.opscloud.domain.vo.base;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/7/7 20:05
 * @Version 1.0
 */
public class ReportVO {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Report implements Serializable {
        @Serial
        private static final long serialVersionUID = -2683596358424500001L;
        @Schema(name = "字段别名")
        private String cName;
        private Integer value;
        private String color;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class CommonReport {
        @Schema(name = "字段别名")
        private String cName;
        private Integer value0;
        private Integer value1;
        private Integer value2;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DailyReport implements Serializable {

        @Serial
        private static final long serialVersionUID = 2779962685525074177L;

        public DailyReport put(String name, List<Report> reports) {
            if (CollectionUtils.isEmpty(reports)) {
                return this;
            }
            Map<String, Report> reportMap = reports.stream().collect(Collectors.toMap(ReportVO.Report::getCName, a -> a, (k1, k2) -> k1));
            List<Integer> values = this.dateCat.stream().map(s -> reportMap.containsKey(s) ? reportMap.get(s).getValue() : Integer.valueOf(0)).collect(Collectors.toList());
            this.valueMap.put(name, values);
            return this;
        }

        @Schema(description = "日期")
        private List<String> dateCat;

        @Schema(description = "日统计数据")
        @Builder.Default
        private Map<String, List<Integer>> valueMap = Maps.newHashMap();

    }


    /**
     * 月报表（堆叠柱形图）
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class MonthlyReport implements Serializable {

        @Serial
        private static final long serialVersionUID = -7788541472908274625L;

        public static void put(MonthlyReport monthlyReport, String name, List<Report> reports) {
            if (CollectionUtils.isEmpty(reports)) {
                return;
            }
            Map<String, Report> reportMap = reports.stream().collect(Collectors.toMap(ReportVO.Report::getCName, a -> a, (k1, k2) -> k1));
            List<Integer> values = monthlyReport.getDateCat().stream().map(s -> reportMap.containsKey(s) ? reportMap.get(s).getValue() : Integer.valueOf(0)).collect(Collectors.toList());
            monthlyReport.getValueMap().put(name, values);
        }

        public MonthlyReport put(String name, List<Report> reports) {
            if (CollectionUtils.isEmpty(reports)) {
                return this;
            }
            Map<String, Report> reportMap = reports.stream().collect(Collectors.toMap(ReportVO.Report::getCName, a -> a, (k1, k2) -> k1));
            List<Integer> values = this.dateCat.stream().map(s -> reportMap.containsKey(s) ? reportMap.get(s).getValue() : Integer.valueOf(0)).collect(Collectors.toList());
            this.valueMap.put(name, values);
            return this;
        }

        @Schema(description = "月份")
        private List<String> dateCat;

        @Schema(description = "月度统计数据")
        @Builder.Default
        private Map<String, List<Integer>> valueMap = Maps.newHashMap();

    }

    /**
     * 月报表（柱形图）
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class MonthReport implements Serializable {

        @Serial
        private static final long serialVersionUID = 2165068012664529432L;

        @Schema(description = "日期(月)")
        private List<String> dateCat;

        @Schema(description = "月度统计")
        private List<Integer> values;

        public static ReportVO.MonthReport buildMonthReport(List<ReportVO.Report> reports) {
            List<String> dateCat = Lists.newArrayList();
            List<Integer> values = Lists.newArrayList();
            reports.forEach(e -> {
                dateCat.add(e.getCName());
                values.add(e.getValue());
            });
            return ReportVO.MonthReport.builder()
                    .dateCat(dateCat)
                    .values(values)
                    .build();
        }

    }

}