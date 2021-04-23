package com.baiyi.opscloud.domain.vo.cloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsInstance;
import com.baiyi.opscloud.domain.generator.opscloud.OcAliyunOnsTopic;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/5 3:25 下午
 * @Since 1.0
 */
public class AliyunONSVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Instance {

        private Integer id;

        @ApiModelProperty(value = "实例id")
        private String instanceId;

        @ApiModelProperty(value = "实例名称")
        private String instanceName;

        @ApiModelProperty(value = "环境名")
        private String envName;


        @ApiModelProperty(value = "实例是否有命名空间")
        private Boolean independentNaming;

        /**
         * 实例状态。取值说明如下：
         * 0：铂金版实例部署中
         * 2：后付费实例已欠费
         * 5：后付费实例或铂金版实例服务中
         * 7：铂金版实例升级中且服务可用
         */
        @ApiModelProperty(value = "实例状态")
        private Integer instanceStatus;

        /**
         * 实例类型。取值说明如下：
         * 1：后付费实例
         * 2：铂金版实例
         */
        @ApiModelProperty(value = "实例类型")
        private Integer instanceType;

        @ApiModelProperty(value = "地区id")
        private String regionId;

        @ApiModelProperty(value = "消息收发 TPS 上限")
        private Long maxTps;

        @ApiModelProperty(value = "该实例下允许创建的 Topic 数量上限")
        private Integer topicCapacity;

        @ApiModelProperty(value = "TCP 协议接入点")
        private String tcpEndpoint;

        @ApiModelProperty(value = "HTTP 公网接入点")
        private String httpInternetEndpoint;

        @ApiModelProperty(value = "HTTPS 公网接入点")
        private String httpInternetSecureEndpoint;

        @ApiModelProperty(value = "HTTP 内网接入点")
        private String httpInternalEndpoint;

        @ApiModelProperty(value = "铂金版实例的过期时间")
        private Long releaseTime;

        @ApiModelProperty(value = "Topic数")
        private Integer topicTotal;

        @ApiModelProperty(value = "GID数")
        private Integer groupIdTotal;

        @ApiModelProperty(value = "备注")
        private String remark;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class InstanceStatistics {

        @ApiModelProperty(value = "实例总数")
        private Integer instanceTotal;

        @ApiModelProperty(value = "Topic总数")
        private Integer topicTotal;

        @ApiModelProperty(value = "Group总数")
        private Integer groupTotal;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Topic {

        private Integer id;

        @ApiModelProperty(value = "实例id")
        private String instanceId;

        @ApiModelProperty(value = "topic")
        private String topic;

        @ApiModelProperty(value = "实例是否有命名空间")
        private Boolean independentNaming;

        /**
         * 消息类型。取值说明如下：
         * 0：普通消息
         * 1：分区顺序消息
         * 2：全局顺序消息
         * 4：事务消息
         * 5：定时/延时消息
         */
        @ApiModelProperty(value = "消息类型")
        private Integer messageType;

        /**
         * 所有关系编号。取值说明如下：
         * 1：持有者
         * 2：可以发布
         * 4：可以订阅
         * 6：可以发布和订阅
         */
        @ApiModelProperty(value = "所有关系编号")
        private Integer relation;

        @Column(name = "relation_name")
        private String relationName;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "备注")
        private String remark;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Group {

        private Integer id;

        @ApiModelProperty(value = "实例id")
        private String instanceId;

        @ApiModelProperty(value = "groupId")
        private String groupId;

        @ApiModelProperty(value = "实例是否有命名空间")
        private Boolean independentNaming;

        /**
         * 查询的 Group ID 适用的协议。TCP 协议和 HTTP 协议的 Group ID 不可以共用，需要分别创建。取值说明如下：
         * tcp：表示该 Group ID 仅适用于 TCP 协议的消息收发。
         * http：表示该 Group ID 仅适用于 HTTP 协议的消息收发。
         */
        @ApiModelProperty(value = "协议类型")
        private String groupType;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "备注")
        private String remark;

        /**
         * 告警状态
         * 0：停用
         * 1：启用
         * -1：异常
         */
        @ApiModelProperty(value = "告警状态")
        private Integer alarmStatus;

        @ApiModelProperty(value = "告警接收人列表")
        private List<OcUser> alarmUserList;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicSubDetail {
        @ApiModelProperty(value = "消息数总和")
        private Long totalCount;
        @ApiModelProperty(value = "最后更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastTimeStamp;

        /**
         * 所有关系编号。取值说明如下：
         * <p>
         * 2：可以发布
         * 4：可以订阅
         * 6：可以发布和订阅
         */
        @ApiModelProperty(value = "所有关系编号")
        private Integer perm;
        @ApiModelProperty(value = "订阅groupId列表")
        private List<TopicSub> subList;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicSub {

        @ApiModelProperty(value = "订阅该Topic的GroupID")
        private String groupId;

        @ApiModelProperty(value = "订阅表达式")
        private String subString;

        /**
         * 消费模式。取值说明如下：
         * <p>
         * CLUSTERING：集群订阅
         * BROADCASTING：广播订阅
         */
        @ApiModelProperty(value = "消费模式")
        private String messageModel;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GroupSubDetail {

        @ApiModelProperty(value = "消费者是否在线")
        private Boolean online;

        /**
         * 消费模式。取值说明如下：
         * <p>
         * CLUSTERING：集群订阅
         * BROADCASTING：广播订阅
         */
        @ApiModelProperty(value = "消费模式")
        private String messageModel;

        private List<GroupSub> subList;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GroupSub {

        @ApiModelProperty(value = "订阅的Topic")
        private String topic;

        @ApiModelProperty(value = "订阅表达式")
        private String subString;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GroupStatus {
        @ApiModelProperty(value = "是否在线")
        private Boolean online;
        @ApiModelProperty(value = "集群总消费堆积")
        private Long totalDiff;
        @ApiModelProperty(value = "总消费TPS")
        private Float consumeTps;
        @ApiModelProperty(value = "最后消费时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastTimestamp;
        @ApiModelProperty(value = "延迟时间")
        private Long delayTime;
        /**
         * 消费模型。取值说明如下：
         * <p>
         * CLUSTERING：集群消费模式
         * BROADCASTING：广播消费模式
         */
        @ApiModelProperty(value = "消费模型")
        private String consumeModel;
        @ApiModelProperty(value = "订阅关系是否一致")
        private Boolean subscriptionSame;
        @ApiModelProperty(value = "rebalance是否正常")
        private Boolean rebalanceOK;
        @ApiModelProperty(value = "实例Id")
        private String instanceId;
        @ApiModelProperty(value = "集群当前在线客户端信息")
        private List<GroupConnection> connectionSet;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GroupConnection {
        private String clientId;
        private String clientAddr;
        private String hostName;
        private String language;
        private String version;
        private String remoteIP;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class GroupAlarm {
        private Integer id;

        @ApiModelProperty(value = "地区id")
        private String regionId;

        @ApiModelProperty(value = "oc_aliyun_ons_group 关联id")
        private Integer onsGroupId;

        @ApiModelProperty(value = "实例id")
        private String instanceId;

        @ApiModelProperty(value = "groupId")
        private String groupId;

        /**
         * 告警状态
         * 0：停用
         * 1：启用
         * -1：异常
         */
        @ApiModelProperty(value = "告警状态")
        private Integer alarmStatus;

        @ApiModelProperty(value = "是否在线")
        private Boolean online;

        @ApiModelProperty(value = "订阅关系是否一致")
        private Boolean subscriptionSame;

        @ApiModelProperty(value = "rebalance是否正常")
        private Boolean rebalanceOk;

        @ApiModelProperty(value = "集群总消费堆积")
        private Long totalDiff;

        @ApiModelProperty(value = "延迟时间")
        private Long delayTime;

        @ApiModelProperty(value = "告警沉默时间")
        private Integer alarmSilentTime;

        @ApiModelProperty(value = "告警接收人列表")
        private List<OcUser> alarmUserList;

        @ApiModelProperty(value = "告警接收人ID列表")
        private List<Integer> userIdList;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicMessage {
        private String topic;
        private Integer flag;
        private String body;
        private Integer storeSize;
        private Long bornTimestamp;
        private String bornTime;
        private String bornHost;
        private Long storeTimestamp;
        private String storeTime;
        private String storeHost;
        private String msgId;
        private String offsetId;
        private Integer bodyCRC;
        private Integer reconsumeTimes;
        private String instanceId;
        private List<TopicMessageProperty> propertyList;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicMessageProperty {
        private String name;
        private String value;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicMessageTraceMap {
        private Long pubTimestamp;
        private String pubTime;
        private String topic;
        private String pubGroupName;
        private String msgId;
        private String tag;
        private String msgKey;
        private String bornHost;
        private String bornHostname;
        private Integer costTime;
        private String status;
        List<TopicMessageSubMap> subList;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicMessageSubMap {
        private String subGroupName;
        private Integer successCount;
        private Integer failCount;
        private Integer count;
        private List<TopicMessageSubClient> clientList;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class TopicMessageSubClient {
        private String subGroupName;
        private Long subTime;
        private String subTimeToStr;
        private String clientHost;
        private String clientHostname;
        private Integer reconsumeTimes;
        private Integer costTime;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class applyInstance {
        private List<OcAliyunOnsInstance> nowInstanceList;
        private List<OcAliyunOnsInstance> selectInstanceList;
        private OcAliyunOnsTopic topic;
        private OcAliyunOnsGroup group;
    }

}
