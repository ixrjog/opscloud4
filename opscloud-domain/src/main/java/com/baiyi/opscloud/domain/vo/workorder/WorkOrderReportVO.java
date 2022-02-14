package com.baiyi.opscloud.domain.vo.workorder;

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
 * @Date 2022/2/14 11:32 AM
 * @Version 1.0
 */
public class WorkOrderReportVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Report {
        private String cName;
        private Integer value;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class MonthReport implements Serializable {
        private static final long serialVersionUID = -7642429717526988404L;
        @ApiModelProperty(value = "日期")
        private List<String> dateCatList;

        @ApiModelProperty(value = "工单名称月度统计")
        private Map<String, List<Integer>> nameStatistics;
    }

}
