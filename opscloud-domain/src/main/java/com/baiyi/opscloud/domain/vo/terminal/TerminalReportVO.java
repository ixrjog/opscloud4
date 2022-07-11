package com.baiyi.opscloud.domain.vo.terminal;

import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/7/7 20:47
 * @Version 1.0
 */
public class TerminalReportVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class TerminalReport implements Serializable {

        private static final long serialVersionUID = 6948099219656277502L;

        @ApiModelProperty(value = "用户总数")
        private Integer userTotal;

        @ApiModelProperty(value = "会话总数")
        private Integer sessionTotal;

        @ApiModelProperty(value = "实例总数")
        private Integer instanceTotal;

        @ApiModelProperty(value = "命令总数")
        private Integer commandTotal;

        private MonthlyReport sessionReport;

        private MonthlyReport instanceReport;

        private MonthReport commandMonthReport;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class MonthlyReport implements Serializable {

        private static final long serialVersionUID = 4131580978973119640L;

        @ApiModelProperty(value = "月份")
        private List<String> dateCat;

        @ApiModelProperty(value = "月度统计数据")
        @Builder.Default
        private Map<String, List<Integer>> valueMap = Maps.newHashMap();

    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class MonthReport implements Serializable {

        private static final long serialVersionUID = 2165068012664529432L;

        @ApiModelProperty(value = "日期(月)")
        private List<String> dateCat;

        @ApiModelProperty(value = "月度统计")
        private List<Integer> values;

        public static TerminalReportVO.MonthReport buildMonthReport(List<ReportVO.Report> reports) {
            List<String> dateCat = Lists.newArrayList();
            List<Integer> values = Lists.newArrayList();
            reports.forEach(e -> {
                dateCat.add(e.getCName());
                values.add(e.getValue());
            });
            return TerminalReportVO.MonthReport.builder()
                    .dateCat(dateCat)
                    .values(values)
                    .build();
        }

    }

}
