package com.baiyi.opscloud.domain.vo.helpdesk;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/4 2:56 下午
 * @Since 1.0
 */
public class HelpdeskReportVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class HelpdeskReport {
        private Integer id;

        @ApiModelProperty(value = "类型")
        private Integer helpdeskType;

        @ApiModelProperty(value = "时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date helpdeskTime;

        @ApiModelProperty(value = "xxxx年xx周")
        private String weeks;

        @ApiModelProperty(value = "总计")
        private Integer helpdeskCnt;

        private String comment;

        @Column(name = "create_time")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @Column(name = "update_time")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;

    }

}
