package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/3/2 12:25 下午
 * @Version 1.0
 */
public class CloudDBDatabaseParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "扩展属性",example = "1")
        private Integer  extend;

        @ApiModelProperty(value = "云数据库类型",example = "1")
        private Integer cloudDbType;

        @ApiModelProperty(value = "查询关键字")
        private String queryName;

        @ApiModelProperty(value = "账户uid")
        private String uid;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "标签id")
        private Integer tagId;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class SlowLogPageQuery extends PageParam {

        @ApiModelProperty(value = "云数据库类型",example = "1")
        private Integer cloudDbType;

        @ApiModelProperty(value = "数据库名称")
        private String dbName;

        @ApiModelProperty(value = "实例ID")
        private String dbInstanceId;

        @ApiModelProperty(value = "查询开始日期，格式：yyyy-MM-ddZ")
        private String startTime;

        @ApiModelProperty(value = "查询结束日期，不能小于查询开始日期，与查询开始日期间隔不超过31天。格式：yyyy-MM-ddZ")
        private String endTime;

    }


}
