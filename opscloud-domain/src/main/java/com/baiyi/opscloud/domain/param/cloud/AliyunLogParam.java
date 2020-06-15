package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/6/13 3:30 下午
 * @Version 1.0
 */
public class AliyunLogParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {
        @ApiModelProperty(value = "查询关键字")
        private String queryName;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ProjectQuery {
        @ApiModelProperty(value = "查询关键字")
        private String queryName;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LogStoreQuery {
        private String projectName;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ConfigQuery {
        private String projectName;

        private String logstoreName;
    }
}
