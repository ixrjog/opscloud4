package com.baiyi.opscloud.domain.vo.leo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/12/6 15:40
 * @Version 1.0
 */
public class LeoDeployingVO {

    // 最大RESTART
    public static final int MAX_RESTART = 2;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class Deploying implements Serializable {
        private static final long serialVersionUID = -2373193776064021868L;
        private String deployType;
        private VerionDetails previousVersion;
        private VerionDetails releaseVersion;
        // 副本数
        private Integer replicas;

        private Boolean isFinish;

        @JsonIgnoreProperties
        private Boolean maxRestartError;

        public void init() {
            if (CollectionUtils.isEmpty(releaseVersion.pods)) {
                this.isFinish = false;
                return;
            }
            long count = releaseVersion.pods.stream().filter(e -> e.isComplete).count();
            this.isFinish = count >= replicas;
        }

        public Boolean isMaxRestartError() {
            if (CollectionUtils.isEmpty(releaseVersion.pods)) return false;
            return releaseVersion.pods.stream().anyMatch(e -> e.getRestartCount() >= MAX_RESTART);
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class VerionDetails implements Serializable {
        private static final long serialVersionUID = -605790384101352067L;
        private String versionName;
        private String versionDesc;
        private String image;
        @Builder.Default
        private List<PodDetails> pods = Lists.newArrayList();

        public void put(PodDetails podDetails) {
            this.pods.add(podDetails);
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class PodDetails implements Serializable {
        private static final long serialVersionUID = 6018564588461613238L;
        private Map<String, String> conditions;
        private String podIP;
        private String hostIP;
        private String phase;
        private String reason;
        // 终结
        private Boolean terminating;

        private String startTime;
        // metadata.name
        private String name;
        // metadata.namespace
        private String namespace;
        // 重启次数
        private Integer restartCount;

        private Boolean isComplete;

        public void init() {
            this.isComplete = conditions.keySet().stream().filter(k -> conditions.get(k).equalsIgnoreCase("True")).count() == 4;
        }
    }

}
