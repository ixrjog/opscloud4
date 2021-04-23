package com.baiyi.opscloud.kafka.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/15 2:22 下午
 * @Since 1.0
 */
public class AliyunKafka {

    @Data
    @NoArgsConstructor
    public static class TopicStatusResponse {

        @JSONField(name = "RequestId")
        private String requestId;

        @JSONField(name = "Message")
        private String message;

        @JSONField(name = "TopicStatus")
        private TopicStatus topicStatus;

        @JSONField(name = "Code")
        private Integer code;

        @JSONField(name = "Success")
        private Boolean success;
    }

    @Data
    @NoArgsConstructor
    public static class TopicStatus {

        @JSONField(name = "LastTimeStamp")
        private Long lastTimeStamp;

        @JSONField(name = "TotalCount")
        private Long totalCount;

        @JSONField(name = "OffsetTable")
        private OffsetTable offsetTable;
    }

    @Data
    @NoArgsConstructor
    public static class OffsetTable {

        @JSONField(name = "OffsetTable")
        private List<Offset> offsetTable;
    }

    @Data
    @NoArgsConstructor
    public static class Offset {

        @JSONField(name = "Partition")
        private Integer partition;

        @JSONField(name = "MaxOffset")
        private Integer maxOffset;

        @JSONField(name = "Topic")
        private String topic;

        @JSONField(name = "LastUpdateTimestamp")
        private Long lastUpdateTimestamp;

        @JSONField(name = "MinOffset")
        private Integer minOffset;

        @JSONField(name = "BrokerOffset")
        private Integer brokerOffset;

        @JSONField(name = "ConsumerOffset")
        private Integer consumerOffset;

        @JSONField(name = "LastTimeStamp")
        private Long lastTimeStamp;
    }

    @Data
    @NoArgsConstructor
    public static class ConsumerProgressResponse {

        @JSONField(name = "RequestId")
        private String requestId;

        @JSONField(name = "Message")
        private String message;

        @JSONField(name = "ConsumerProgress")
        private ConsumerProgress consumerProgress;

        @JSONField(name = "Code")
        private Integer code;

        @JSONField(name = "Success")
        private Boolean success;
    }

    @Data
    @NoArgsConstructor
    public static class ConsumerProgress {

        @JSONField(name = "LastTimeStamp")
        private Long lastTimeStamp;

        @JSONField(name = "TotalDiff")
        private Long totalDiff;

        @JSONField(name = "TopicList")
        private TopicList topicList;
    }

    @Data
    @NoArgsConstructor
    public static class TopicList {

        @JSONField(name = "TopicList")
        private List<TopicListData> topicList;
    }


    @Data
    @NoArgsConstructor
    public static class TopicListData {

        @JSONField(name = "LastTimeStamp")
        private Long lastTimeStamp;

        @JSONField(name = "TotalDiff")
        private Long totalDiff;

        @JSONField(name = "OffsetTable")
        private OffsetTable offsetTable;
    }


    @Data
    @NoArgsConstructor
    public static class GroupListResponse {

        @JSONField(name = "RequestId")
        private String requestId;

        @JSONField(name = "Message")
        private String message;

        @JSONField(name = "ConsumerList")
        private ConsumerList consumerList;

        @JSONField(name = "Code")
        private Integer code;

        @JSONField(name = "Success")
        private Boolean success;
    }


    @Data
    @NoArgsConstructor
    public static class ConsumerList {

        @JSONField(name = "ConsumerVO")
        private List<ConsumerVO> consumerVO;

    }

    @Data
    @NoArgsConstructor
    public static class ConsumerVO {

        @JSONField(name = "InstanceId")
        private String instanceId;

        @JSONField(name = "ConsumerId")
        private String consumerId;

        @JSONField(name = "RegionId")
        private String regionId;

        @JSONField(name = "Remark")
        private String remark;
    }


}
