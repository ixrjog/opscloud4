package com.baiyi.opscloud.domain.vo.workevent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
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
    @Schema
    public static class WeeklyReport implements Serializable {

        @Serial
        private static final long serialVersionUID = -6140886856564417436L;

        @Schema(description = "周")
        private List<String> weeks;

        @Schema(description = "周统计数据")
        private Map<String, WeeklyStatistics> valueMap;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class WeeklyStatistics implements Serializable {

        @Serial
        private static final long serialVersionUID = -6140886856564417436L;

        @Schema(description = "周统计")
        private List<Integer> values;

        @Schema(description = "类目颜色")
        private String color;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ItemReport implements Serializable {

        @Serial
        private static final long serialVersionUID = -64254441708424804L;

        private String cName;
        private Integer value;
        private String color;

    }

}