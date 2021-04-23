package com.baiyi.opscloud.domain.vo.export;

import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/4 6:05 下午
 * @Since 1.0
 */
public class ExportVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Task {

        private Integer id;

        private String fileName;

        private Integer taskType;

        private Integer taskStatus;

        private String username;

        @ApiModelProperty(value = "任务开始时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;

        @ApiModelProperty(value = "任务结束时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;

        private OcUser ocUser;
    }
}
