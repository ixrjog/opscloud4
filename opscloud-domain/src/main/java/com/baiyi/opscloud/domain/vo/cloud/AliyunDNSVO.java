package com.baiyi.opscloud.domain.vo.cloud;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 4:13 下午
 * @Since 1.0
 */
public class AliyunDNSVO {

    @Data
    @ApiModel
    public static class Record {

        private Integer id;

        @ApiModelProperty(value = "域名名称")
        private String domainName;

        @ApiModelProperty(value = "解析记录ID")
        private String recordId;

        @ApiModelProperty(value = "主机记录")
        private String recordRr;

        @ApiModelProperty(value = "记录类型")
        private String recordType;

        @ApiModelProperty(value = "记录值")
        private String recordValue;

        @ApiModelProperty(value = "生存时间")
        private Long recordTtl;

        @ApiModelProperty(value = "MX记录的优先级")
        private Long recordPriority;

        @ApiModelProperty(value = "解析线路")
        private String recordLine;

        @ApiModelProperty(value = "当前的解析记录状态")
        private String recordStatus;

        @ApiModelProperty(value = "当前解析记录锁定状态")
        private String recordLocked;

        @ApiModelProperty(value = "负载均衡权重")
        private Integer recordWeight;

        @ApiModelProperty(value = "域名备注")
        private String remark;
    }
}
