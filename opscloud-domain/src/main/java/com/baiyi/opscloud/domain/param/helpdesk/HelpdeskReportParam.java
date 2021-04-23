package com.baiyi.opscloud.domain.param.helpdesk;

import com.baiyi.opscloud.domain.generator.opscloud.OcHelpdeskReport;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/4 2:37 下午
 * @Since 1.0
 */
public class HelpdeskReportParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @NotNull
        @ApiModelProperty(value = "类型")
        private Integer helpdeskType;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class SaveHelpdeskReport {

        private List<OcHelpdeskReport> helpdeskReportList;
    }
}
