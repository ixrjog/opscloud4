package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/9 17:46
 * @Version 1.0
 */
public class LeoBuildVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class Build implements Serializable, ReadableTime.IAgo, ReadableTime.IRuntime {

        private static final long serialVersionUID = -697201191162725310L;

        @ApiModelProperty(value = "构建详情")
        private Object buildDetails;

        @ApiModelProperty(value = "流水线")
        private Pipeline pipeline;

        @ApiModelProperty(value = "以前")
        private String ago;

        @ApiModelProperty(value = "运行时长")
        private String runtime;

        @ApiModelProperty(value = "镜像存在")
        private Boolean isImageExists;

        @ApiModelProperty(value = "镜像")
        private LeoBuildVO.Image image;

        private Integer id;
        private Integer jobId;
        private String jobName;
        private String buildJobName;
        private Integer applicationId;
        private Integer buildNumber;
        private String versionName;
        private String versionDesc;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;
        private String buildStatus;
        private String buildResult;
        private Boolean isFinish;
        private Boolean isDeletedBuildJob;
        private Integer executionType;
        private String pipelineContent;
        private String username;
        private String displayName;
        private String buildConfig;
        private Boolean isActive;
        private String comment;

        @Override
        public Date getAgoTime() {
            if (this.startTime != null) {
                return this.startTime;
            }
            return new Date();
        }
    }

    /**
     * 最新构建信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class LatestBuildInfo implements Serializable {

        private static final long serialVersionUID = -7617778439412190882L;

        private Integer buildId;
        private Integer buildNumber;
        private Boolean running;
        private String color;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class BranchOptions implements Serializable {

        private static final long serialVersionUID = 6999931515242829844L;

        public static final BranchOptions EMPTY_OPTIONS = BranchOptions.builder().build();

        @Builder.Default
        private List<Option> options = Lists.newArrayList();
    }

    @Data
    @Builder
    @ApiModel
    public static class Option implements Serializable {

        private static final long serialVersionUID = -6482052319954322970L;

        private String label;
        private List<Children> options;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class Children implements Serializable {

        private static final long serialVersionUID = -1561200881442892379L;

        private String value;
        private String label;

        private String desc;

        private String commitId;
        private String commitMessage;
    }

    @Data
    @Builder
    @ApiModel
    @AllArgsConstructor
    public static class BranchOrTag implements Serializable {

        private static final long serialVersionUID = 8033652452171334565L;

        private String name;
        private String message;
        private String commit;
        private String commitMessage;
        private String commitUrl;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Pipeline implements Serializable {

        private static final long serialVersionUID = -2644838767573635251L;

        private List<Node> nodes;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Node implements Serializable {

        public static final Node QUEUE = Node.builder()
                .name("Queue")
                .state("PAUSED")
                .build();

        public static final Node INVALID = Node.builder()
                .name("Invalid")
                .state("SKIPPED")
                .build();

        private static final long serialVersionUID = -1465972308441846486L;
        private String firstParent;
        private String name;
        private String state;
        @Builder.Default
        private Integer completePercent = 100;
        private String id;
        @Builder.Default
        private String type = "STAGE";
        @Builder.Default
        private List<Node> children = Lists.newArrayList();

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Image implements Serializable {

        private static final long serialVersionUID = -8085991676738506575L;
        private Integer id;
        private Integer buildId;
        private Integer jobId;
        private String image;
        private String versionName;
        private String versionDesc;
        private Boolean isActive;
        private Date createTime;
        private Date updateTime;

    }

}
