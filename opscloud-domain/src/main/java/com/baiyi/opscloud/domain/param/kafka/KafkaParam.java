package com.baiyi.opscloud.domain.param.kafka;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/14 10:28 上午
 * @Since 1.0
 */
public class KafkaParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicQuery {

        @NotNull(message = "集群名称不能为空")
        @ApiModelProperty(value = "集群名称")
        private String instanceName;

        @NotNull(message = "Topic不能为空")
        @ApiModelProperty(value = "Topic")
        private String topic;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicCreate {

        @NotNull(message = "集群名称不能为空")
        @ApiModelProperty(value = "集群名称")
        private String instanceName;

        @NotNull(message = "Topic不能为空")
        @ApiModelProperty(value = "Topic")
        private String topic;

        @Min(value = 1, message = "分区数量必须大于0")
        @ApiModelProperty(value = "分区数量")
        private Integer partitionNum;

        @ApiModelProperty(value = "备注")
        private String remark;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicModify {

        @NotNull(message = "集群名称不能为空")
        @ApiModelProperty(value = "集群名称")
        private String instanceName;

        @NotNull(message = "Topic不能为空")
        @ApiModelProperty(value = "Topic")
        private String topic;

        @NotNull(message = "新增分区数量不能为空")
        @ApiModelProperty(value = "新增分区数量")
        private Integer addPartitionNum;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GroupCreate {

        @NotNull(message = "集群名称不能为空")
        @ApiModelProperty(value = "集群名称")
        private String instanceName;

        @NotNull(message = "consumerId不能为空")
        @ApiModelProperty(value = "consumerId")
        private String consumerId;

        @ApiModelProperty(value = "备注")
        private String remark;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GroupQuery {

        @NotNull(message = "集群名称不能为空")
        @ApiModelProperty(value = "集群名称")
        private String instanceName;

        @NotNull(message = "consumerId不能为空")
        @ApiModelProperty(value = "consumerId")
        private String consumerId;
    }
}
