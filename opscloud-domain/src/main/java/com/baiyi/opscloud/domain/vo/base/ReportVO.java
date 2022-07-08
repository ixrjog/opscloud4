package com.baiyi.opscloud.domain.vo.base;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/7/7 20:05
 * @Version 1.0
 */
public class ReportVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Report {
        private String cName;
        private Integer value;
    }

}
