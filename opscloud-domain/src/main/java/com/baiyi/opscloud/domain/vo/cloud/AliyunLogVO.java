package com.baiyi.opscloud.domain.vo.cloud;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/6/13 3:32 下午
 * @Version 1.0
 */
public class AliyunLogVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Log {

        private Integer memberSize;

        private Integer id;
        private String accountUid;
        @NotNull(message = "project不能为空")
        private String project;
        @NotNull(message = "logstore不能为空")
        private String logstore;
        @NotNull(message = "config不能为空")
        private String config;
        private String comment;
        private Date createTime;
        private Date updateTime;

    }
}
