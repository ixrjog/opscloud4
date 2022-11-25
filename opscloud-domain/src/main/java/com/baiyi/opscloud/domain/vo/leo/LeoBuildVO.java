package com.baiyi.opscloud.domain.vo.leo;

import com.baiyi.opscloud.domain.vo.base.ShowTime;
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
    public static class Build implements Serializable, ShowTime.IAgo {

        private static final long serialVersionUID = -697201191162725310L;

        @ApiModelProperty(value = "构建对象")
        private Object buildDetails;

        private String ago;

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
        private String username;
        private String displayName;
        private String buildConfig;
        private String comment;

        @Override
        public Date getAgoTime() {
            return this.startTime;
        }

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

}
