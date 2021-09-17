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
 * @Date 2021/9/16 5:39 下午
 * @Version 1.0
 */
public class AliyunLogParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AliyunLogPageQuery extends PageParam implements IExtend {
        @NotNull(message = "必须指定数据源实例id")
        private Integer instanceId;

        @ApiModelProperty(value = "查询关键字")
        private String queryName;

        private Boolean extend;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ProjectQuery {

        @NotNull(message = "必须指定数据源实例id")
        private Integer instanceId;

        @ApiModelProperty(value = "查询关键字")
        private String queryName;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LogStoreQuery {
        @NotNull(message = "必须指定数据源实例id")
        private Integer instanceId;
        private String projectName;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ConfigQuery {
        @NotNull(message = "必须指定数据源实例id")
        private Integer instanceId;
        private String projectName;
        private String logstoreName;
    }
}
