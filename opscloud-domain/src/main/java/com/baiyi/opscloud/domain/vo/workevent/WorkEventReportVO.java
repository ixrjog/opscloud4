package com.baiyi.opscloud.domain.vo.workevent;

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
 * @Author 修远
 * @Date 2022/8/22 3:52 PM
 * @Since 1.0
 */
public class WorkEventReportVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class WeeklyReport implements Serializable {

        private static final long serialVersionUID = -6140886856564417436L;

        @ApiModelProperty(value = "周")
        private List<String> weeks;

        @ApiModelProperty(value = "周统计数据")
        private Map<String, WeeklyStatistics> valueMap;


    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class WeeklyStatistics implements Serializable {

        private static final long serialVersionUID = -6140886856564417436L;

        @ApiModelProperty(value = "周统计")
        private List<Integer> values;

        @ApiModelProperty(value = "类目颜色")
        private String color;


    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class ItemReport implements Serializable {

        private static final long serialVersionUID = -64254441708424804L;

        private String cName;
        private Integer value;
        private String color;

    }

}
