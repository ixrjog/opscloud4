package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/9 11:35 上午
 * @Since 1.0
 */
public class AliyunONSParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryInstanceDetail {

        @NotNull
        private String regionId;

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryTopicList {

        @NotNull
        private String regionId;

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryTopic {

        @NotNull
        private String regionId;

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

        @NotNull(message = "topic不能为空")
        @ApiModelProperty(value = "topic")
        private String topic;


    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryGroupList {

        @NotNull
        private String regionId;

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryGroup {

        @NotNull
        private String regionId;

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

        @NotNull(message = "groupId不能为空")
        @ApiModelProperty(value = "groupId")
        private String groupId;

        @NotNull(message = "协议类型不能为空")
        @ApiModelProperty(value = "协议类型")
        private String groupType;

    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryTopicSubDetail {

        @NotNull
        private String regionId;

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

        @NotNull(message = "topic不能为空")
        @ApiModelProperty(value = "topic")
        private String topic;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryGroupSubDetail {

        @NotNull
        private String regionId;

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

        @NotNull(message = "groupId不能为空")
        @ApiModelProperty(value = "groupId")
        private String groupId;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicCreate {

        @NotNull
        private String regionId;

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

        @NotNull(message = "消息类型不能为空")
        @ApiModelProperty(value = "消息类型")
        private Integer messageType;

        @NotNull(message = "topic不能为空")
        @ApiModelProperty(value = "topic")
        private String topic;

        @ApiModelProperty(value = "备注")
        private String remark;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GroupCreate {

        @NotNull
        private String regionId;

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

        @NotNull(message = "协议类型不能为空")
        @ApiModelProperty(value = "协议类型")
        private String groupType;

        @NotNull(message = "groupId不能为空")
        @ApiModelProperty(value = "groupId")
        private String groupId;

        @ApiModelProperty(value = "备注")
        private String remark;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicPageQuery extends PageParam {

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

        @ApiModelProperty(value = "topic")
        private String topic;

        @ApiModelProperty(value = "消息类型")
        private Integer messageType;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GroupPageQuery extends PageParam {

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

        @ApiModelProperty(value = "groupId")
        private String groupId;

        @ApiModelProperty(value = "协议类型")
        private String groupType;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicCheck {

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

        @NotNull(message = "topic不能为空")
        @ApiModelProperty(value = "topic")
        private String topic;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GroupCheck {

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

        @NotNull(message = "topic不能为空")
        @ApiModelProperty(value = "topic")
        private String groupId;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryTopicMsg {

        @NotNull
        private String regionId;

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

        @NotNull(message = "Topic不能为空")
        @ApiModelProperty(value = "Topic")
        private String topic;

        @NotNull(message = "Message ID不能为空")
        @ApiModelProperty(value = "Message ID")
        private String msgId;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryTrace {

        @NotNull
        private String regionId;

        @NotNull(message = "ons实例id不能为空")
        @ApiModelProperty(value = "ons实例id")
        private String instanceId;

        @NotNull(message = "Topic不能为空")
        @ApiModelProperty(value = "Topic")
        private String topic;

        @NotNull(message = "Message ID不能为空")
        @ApiModelProperty(value = "Message ID")
        private String msgId;

        @NotNull(message = "开始时间不能为空")
        @ApiModelProperty(value = "开始时间")
        private Long beginTime;

        @NotNull(message = "结束时间不能为空")
        @ApiModelProperty(value = "结束时间")
        private Long endTime;


    }
}
