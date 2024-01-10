package com.baiyi.opscloud.domain.param.datasource;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/9/16 5:39 下午
 * @Version 1.0
 */
public class AliyunLogParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class AliyunLogPageQuery extends PageParam implements IExtend {
        @NotNull(message = "必须指定数据源实例ID")
        private Integer instanceId;

        @Schema(description = "查询关键字")
        private String queryName;

        private Boolean extend;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class ProjectQuery {

        @NotNull(message = "必须指定数据源实例ID")
        private Integer instanceId;

        @Schema(description = "查询关键字")
        private String queryName;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class LogStoreQuery {
        @NotNull(message = "必须指定数据源实例ID")
        private Integer instanceId;
        private String projectName;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class ConfigQuery {
        @NotNull(message = "必须指定数据源实例ID")
        private Integer instanceId;
        private String projectName;
        private String logstoreName;
    }

}