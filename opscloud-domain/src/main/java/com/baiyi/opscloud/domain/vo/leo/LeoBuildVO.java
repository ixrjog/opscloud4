package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import com.baiyi.opscloud.domain.vo.common.IHeartbeat;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    @Schema
    public static class Build implements Serializable, ReadableTime.IAgo, ReadableTime.IRuntime, TagVO.ITags, IHeartbeat {

        @Serial
        private static final long serialVersionUID = -697201191162725310L;

        // ITags
        @Override
        public Integer getBusinessId() {
            return this.id;
        }

        private final Integer businessType = BusinessTypeEnum.LEO_BUILD.getType();

        private List<TagVO.Tag> tags;

        @Schema(description = "构建日志")
        private List<LeoLogVO.Log> buildLogs;

        @Schema(description = "存活")
        private Boolean isLive;

        @Schema(description = "构建详情")
        private Object buildDetails;

        @Schema(description = "流水线")
        private Pipeline pipeline;

        @Schema(description = "以前")
        private String ago;

        @Schema(description = "运行时长")
        private String runtime;

        @Schema(description = "镜像存在")
        private Boolean isImageExists;

        @Schema(description = "镜像")
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
        private String ocInstance;
        @Schema(description = "工单ID,默认值0")
        private Integer ticketId;

        @Override
        public Date getAgoTime() {
            return Objects.requireNonNullElseGet(this.startTime, Date::new);
        }

    }

    /**
     * 最新构建信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class LatestBuildInfo implements Serializable {

        @Serial
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
    @Schema
    public static class BranchOptions implements Serializable {

        @Serial
        private static final long serialVersionUID = 6999931515242829844L;

        public static final BranchOptions EMPTY_OPTIONS = BranchOptions.builder().build();

        @Builder.Default
        private List<Option> options = Lists.newArrayList();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MavenPublishInfo implements Serializable {

        @Serial
        private static final long serialVersionUID = -2414452655205061922L;

        public static final MavenPublishInfo EMPTY_INFO = MavenPublishInfo.builder().build();

        private String artifactId;

        private String groupId;

        private String version;

    }

    @Data
    @Builder
    @Schema
    public static class Option implements Serializable {

        @Serial
        private static final long serialVersionUID = -6482052319954322970L;

        private String label;
        private List<Children> options;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Children implements ReadableTime.IAgo, Serializable {

        @Serial
        private static final long serialVersionUID = -1561200881442892379L;

        private String value;
        private String label;

        private String desc;

        private String commitId;
        private String commitMessage;
        private String commitWebUrl;

        private String authorName;
        private String authorEmail;
        private Date authoredDate;

        private String ago;

        @Override
        public Date getAgoTime() {
            return authoredDate;
        }

    }

    @Data
    @Builder
    @Schema
    @AllArgsConstructor
    public static class BranchOrTag implements ReadableTime.IAgo, Serializable {

        @Serial
        private static final long serialVersionUID = 8033652452171334565L;

        private String name;
        private String message;
        private String commit;
        private String commitMessage;
        private String commitWebUrl;
        private String authorName;
        private String authorEmail;
        private Date authoredDate;

        private String ago;

        @Override
        public Date getAgoTime() {
            return authoredDate;
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Pipeline implements Serializable {

        @Serial
        private static final long serialVersionUID = -2644838767573635251L;

        private List<Node> nodes;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Node implements Serializable {

        public static final Node QUEUE = Node.builder()
                .name("Queue")
                .state("PAUSED")
                .build();

        public static final Node INVALID = Node.builder()
                .name("Invalid")
                .state("SKIPPED")
                .build();

        @Serial
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
    @Schema
    public static class Image implements Serializable {

        @Serial
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CompareResults implements Serializable {
        @Serial
        private static final long serialVersionUID = 2408573959847040385L;

        private Commit commit;
        private List<Commit> commits;
        // private List<Diff> diffs;
        private Boolean compareTimeout;
        private Boolean compareSameRef;

        private Boolean success;

        private String msg;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Commit implements Serializable {

        @Serial

        private static final long serialVersionUID = 2434593315783430166L;
        // private Author author;
        private Date authoredDate;
        private String authorEmail;
        private String authorName;
        private Date committedDate;
        private String committerEmail;
        private String committerName;
        private Date createdAt;
        private String id;
        private String message;
        private List<String> parentIds;
        private String shortId;
        // private CommitStats stats;
        private String status;
        private Date timestamp;
        private String title;
        private String url;
        private String webUrl;
        private Long projectId;
        private Pipeline lastPipeline;

    }

}