package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.constants.DeployTypeConstants;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/12/6 15:40
 * @Version 1.0
 */
public class LeoDeployingVO {

    /**
     * 最大RESTART
     */
    public static final int MAX_RESTART = 2;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Deploying implements Serializable {

        @Serial
        private static final long serialVersionUID = -2373193776064021868L;
        private String deployType;

        // previous/release
        private VersionDetails versionDetails1;
        private VersionDetails versionDetails2;
        // 副本数
        private Integer replicas;

        @Builder.Default
        private Boolean isFinish = false;

        @JsonIgnoreProperties
        private Boolean maxRestartError;

        /**
         * init with deployType
         */
        public Deploying init() {
            if (DeployTypeConstants.ROLLING.name().equals(deployType) || DeployTypeConstants.REDEPLOY.name().equals(deployType)) {
                // 没有新版本
                if (CollectionUtils.isEmpty(versionDetails2.pods)) {
                    return this;
                }
                // 老版本为空
                if (CollectionUtils.isEmpty(this.versionDetails1.pods)) {
                    // 判断完成启动的新版本副本数是否达成
                    long count = versionDetails2.pods.stream().filter(e -> e.isComplete).count();
                    this.isFinish = count >= replicas;
                }
            }
            if (DeployTypeConstants.OFFLINE.name().equals(deployType)) {
                this.isFinish = CollectionUtils.isEmpty(this.versionDetails1.pods);
            }
            return this;
        }

        public Boolean isMaxRestartError() {
            if (versionDetails2 == null) {
                return false;
            }
            if (CollectionUtils.isEmpty(versionDetails2.pods)) {
                return false;
            }
            return versionDetails2.pods.stream().anyMatch(e -> e.getRestartCount() >= MAX_RESTART);
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class VersionDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = -605790384101352067L;

        public static final VersionDetails NO_SHOW = VersionDetails.builder()
                .show(false)
                .build();

        private String title;

        @Builder.Default
        private String versionName = "-";
        @Builder.Default
        private String versionDesc = "";
        private String image;
        @Builder.Default
        private List<PodDetails> pods = Lists.newArrayList();

        @Builder.Default
        private Boolean show = true;

        public void putPod(PodDetails podDetails) {
            this.pods.add(podDetails);
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    @JsonIgnoreProperties(ignoreUnknown = true)
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public static class PodDetails implements Serializable, ReadableTime.IAgo {

        @Serial
        private static final long serialVersionUID = 6072373268821025901L;

        private Map<String, String> conditions;

        private String podIP;
        private String hostIP;
        private String phase;
        private String reason;
        // 终结
        private Boolean terminating;

        private String ago;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;
        // metadata.name
        private String name;
        // metadata.namespace
        private String namespace;
        // 重启次数
        private Integer restartCount;

        private Boolean isComplete;

        public PodDetails init() {
            this.isComplete = conditions.keySet()
                    .stream()
                    .filter(k -> "True".equalsIgnoreCase(conditions.get(k)))
                    .count() == 4;
            return this;
        }

        @Override
        public Date getAgoTime() {
            return getStartTime();
        }

    }

}