package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 3:04 下午
 * @Since 1.0
 */
public class AliyunDomainRecordParam {

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "域名")
        private String domainName;

        @ApiModelProperty(value = "查询关键字")
        private String queryName;

        @ApiModelProperty(value = "记录类型")
        private String recordType;

        @ApiModelProperty(value = "状态")
        private String recordStatus;

    }

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class AddDomainRecord {

        @ApiModelProperty(value = "域名名称")
        @NotNull(message = "域名名称不能为空")
        private String domainName;

        @ApiModelProperty(value = "主机记录")
        @NotNull(message = "主机记录不能为空")
        private String recordRr;

        @ApiModelProperty(value = "记录类型")
        @NotNull(message = "记录类型不能为空")
        private String recordType;

        @ApiModelProperty(value = "记录值")
        @NotNull(message = "记录值不能为空")
        private String recordValue;

    }

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class UpdateDomainRecord {

        @ApiModelProperty(value = "解析记录的ID")
        @NotNull(message = "解析记录的ID不能为空")
        private String recordId;

        @ApiModelProperty(value = "主机记录")
        @NotNull(message = "主机记录不能为空")
        private String recordRr;

        @ApiModelProperty(value = "记录类型")
        @NotNull(message = "记录类型不能为空")
        private String recordType;

        @ApiModelProperty(value = "记录值")
        @NotNull(message = "记录值不能为空")
        private String recordValue;

    }

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class SetDomainRecordStatus {

        @ApiModelProperty(value = "解析记录的ID")
        @NotNull(message = "解析记录的ID不能为空")
        private String recordId;

        @ApiModelProperty(value = "状态")
        @NotNull(message = "状态不能为空")
        private String recordStatus;

    }
}
