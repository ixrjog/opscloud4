package com.baiyi.opscloud.domain.vo.workorder;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private String name;
        private Integer value;
    }
}
