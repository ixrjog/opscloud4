package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/1/11 18:20
 * @Version 1.0
 */
public class LeoReportVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class LeoReport implements Serializable {

        private static final long serialVersionUID = 5737296480391365951L;

        @ApiModelProperty(value = "构建月报表")
        private MonthReport buildMonthReport;

        @ApiModelProperty(value = "部署月报表")
        private MonthReport deployMonthReport;

        private List<LeoJenkinsInstance> instances;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class MonthReport implements Serializable {

        private static final long serialVersionUID = 1759830997901199516L;

        @ApiModelProperty(value = "日期(月)")
        private List<String> dateCat;

        @ApiModelProperty(value = "月度统计")
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
    @ApiModel
    public static class LeoJenkinsInstance implements Serializable {

        private static final long serialVersionUID = -8703855218611943799L;

        @ApiModelProperty(value = "数据源实例ID")
        private Integer instanceId;

        @ApiModelProperty(value = "数据源实例名称")
        private String instanceName;

        @ApiModelProperty(value = "数据源实例标签")
        private List<TagVO.Tag> tags;

    }

}
