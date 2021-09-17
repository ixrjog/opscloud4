package com.baiyi.opscloud.domain.vo.datasource.aliyun;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2021/9/16 4:59 下午
 * @Version 1.0
 */
public class AliyunLogVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Log extends BaseVO {

        @ApiModelProperty(value = "成员数量")
        private Integer memberSize;

        private Integer id;

        @NotNull
        private Integer datasourceInstanceId;

        @NotNull
        private String project;

        @NotNull
        private String logstore;

        @NotNull
        private String config;

        private String comment;

    }

}
