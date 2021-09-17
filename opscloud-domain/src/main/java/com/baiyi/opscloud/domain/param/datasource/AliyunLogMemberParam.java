package com.baiyi.opscloud.domain.param.datasource;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/6/15 9:18 上午
 * @Version 1.0
 */
public class AliyunLogMemberParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LogMemberPageQuery extends PageParam implements IExtend {

        @NotNull(message = "必须指定日志服务id")
        private Integer aliyunLogId;

        @ApiModelProperty(value = "查询关键字")
        private String queryName;

        private Boolean extend;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AddLogMember {

        @NotNull(message = "必须指定日志服务id")
        private Integer aliyunLogId;

        @NotNull(message = "必须指定服务器组id")
        private Integer serverGroupId;

        private String serverGroupName;

        private String topic;

        @NotNull(message = "必须指定环境")
        private Integer envType;

        private String comment;
    }
}
