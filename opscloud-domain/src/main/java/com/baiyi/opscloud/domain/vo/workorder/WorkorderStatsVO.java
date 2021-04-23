package com.baiyi.opscloud.domain.vo.workorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/25 2:38 下午
 * @Since 1.0
 */
public class WorkorderStatsVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BaseStatsData {
        @ApiModelProperty(value = "名称")
        private String name;

        @ApiModelProperty(value = "数量")
        private Integer value;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class WorkorderMonthStats implements Serializable {
        private static final long serialVersionUID = -7642429717526988404L;
        @ApiModelProperty(value = "日期")
        private List<String> dateCatList;

        @ApiModelProperty(value = "工单名称月度统计")
        private Map<String, List<Integer>> nameStatistics;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class WorkorderMonthStatsData {
        @ApiModelProperty(value = "日期")
        private String dateCat;

        @ApiModelProperty(value = "总计")
        private Integer value;
    }
}
