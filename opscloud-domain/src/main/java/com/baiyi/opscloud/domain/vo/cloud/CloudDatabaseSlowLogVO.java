package com.baiyi.opscloud.domain.vo.cloud;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/4/3 4:28 下午
 * @Version 1.0
 */
public class CloudDatabaseSlowLogVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class SlowLog {

        @ApiModelProperty(value = "慢查询汇总标识ID", example = "26584213")
        private Long SlowLogId;

        @ApiModelProperty(value = "数据库名称")
        private String DBName;

        @ApiModelProperty(value = "SQL语句")
        private String SQLText;

        @ApiModelProperty(value = "MySQL总执行次数", example = "1")
        private Long MySQLTotalExecutionCounts;

        @ApiModelProperty(value = "MySQL总执行时间，单位：秒", example = "1")
        private Long MySQLTotalExecutionTimes;

        @ApiModelProperty(value = "锁定总时长，单位：秒", example = "1")
        private Long TotalLockTimes;

        @ApiModelProperty(value = "最大锁定时长，单位：秒", example = "1")
        private Long MaxLockTime;

        @ApiModelProperty(value = "解析SQL总行数", example = "1")
        private Long ParseTotalRowCounts;

        @ApiModelProperty(value = "解析SQL最大行数", example = "1")
        private Long ParseMaxRowCount;

        @ApiModelProperty(value = "返回SQL总行数", example = "1")
        private Long ReturnTotalRowCounts;

        @ApiModelProperty(value = "返回SQL最大行数", example = "1")
        private Long ReturnMaxRowCount;

        @ApiModelProperty(value = "数据生成日期,2011-05-30Z")
        private String CreateTime;

        @ApiModelProperty(value = "数据报表生成日期,2011-05-30Z")
        private String ReportTime;

        @ApiModelProperty(value = "最大执行时长，单位：秒。", example = "60")
        private Long MaxExecutionTime;

        @ApiModelProperty(value = "平均执行时间，单位：秒。", example = "1")
        private Long AvgExecutionTime;

    }

}
