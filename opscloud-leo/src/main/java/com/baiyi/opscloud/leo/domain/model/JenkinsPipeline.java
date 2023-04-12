package com.baiyi.opscloud.leo.domain.model;

import com.baiyi.opscloud.leo.converter.JenkinsPipelineConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/25 16:54
 * @Version 1.0
 */
public class JenkinsPipeline {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Node implements Serializable {

        @Serial
        private static final long serialVersionUID = -498230742918409089L;

//        public static final Node QUEUE = Node.builder().build();
//
//        public static final Node INVALID = Node.builder()
//                .displayName("Invalid")
//                .build();

        /**
         * actions: []
         * causeOfBlockage: null
         * displayDescription: null
         * displayName: "检出项目"
         * durationInMillis: 433
         * edges: [,…]
         * 0: {_class: "io.jenkins.blueocean.rest.impl.pipeline.PipelineNodeImpl$EdgeImpl", id: "11", type: "STAGE"}
         * id: "11"
         * type: "STAGE"
         * _class: "io.jenkins.blueocean.rest.impl.pipeline.PipelineNodeImpl$EdgeImpl"
         * firstParent: null
         * id: "6"
         * input: null
         * restartable: true
         * result: "SUCCESS"
         * startTime: "2021-03-01T14:40:07.720+0800"
         * state: "FINISHED"
         * type: "STAGE"
         */
        private String causeOfBlockage;
        private String displayDescription;
        @Builder.Default
        private String displayName = "Queue";
        private Integer durationInMillis;
        private String firstParent;
        @Builder.Default
        private String id = String.valueOf(1);
        private Boolean restartable;
        private String result;
        //   shape = JsonFormat.Shape.STRING       timezone = "GMT+8"                                           2021-03-01T14:40:07.720+0800
        // @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm.SSS'Z'")
        private String startTime;
        @Builder.Default
        private String state = JenkinsPipelineConverter.States.PAUSED;
        private String type;
        private List<Node> children;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Step implements Serializable {

        @Serial
        private static final long serialVersionUID = -8565646505496192779L;

        private String displayDescription;
        private String displayName;
        private Long durationInMillis;
        private String id;
        private String result;
        private String state;
        private String type;
        private String log;

    }

}