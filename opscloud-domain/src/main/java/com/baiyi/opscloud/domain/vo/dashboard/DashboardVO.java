package com.baiyi.opscloud.domain.vo.dashboard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/11/23 11:33 上午
 * @Version 1.0
 */
public class DashboardVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class HelpDeskReportGroupByWeeks implements Serializable {

        private static final long serialVersionUID = 6144238177966675381L;
        @ApiModelProperty(value = "按周统计")
        private  List<HeplDeskGroupByWeek>  heplDeskGroupByWeeks;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class HelpDeskReportGroupByTypes implements Serializable {

        private static final long serialVersionUID = 6144238177966675381L;
        @ApiModelProperty(value = "按类型统计")
        private  List<HeplDeskGroupByType> heplDeskGroupByTypes;

    }

}
