package com.baiyi.opscloud.domain.vo.ser;

import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author 修远
 * @Date 2023/6/12 3:03 PM
 * @Since 1.0
 */
public class SerDeployVO {

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Task extends BaseVO {

        private Integer id;

        private Integer applicationId;

        private Application application;

        private String taskUuid;

        private String taskName;

        private String taskDesc;

        private Boolean isActive;

        private Boolean isFinish;

        private List<TaskItem> taskItemList;

        private Integer taskItemSize;

        private List<SubTask> subTaskList;

        private Integer subTaskSize;

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class TaskItem extends BaseVO {

        private Integer id;

        private Integer serDeployTaskId;

        private String itemName;

        private String itemKey;

        private String itemBucketName;

        private String itemMd5;

        private String itemSize;

        private String reloadUsername;

        private User reloadUser;

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SubTask extends BaseVO implements ReadableTime.IAgo, ReadableTime.IRuntime {

        private Integer id;

        private Integer serDeployTaskId;

        private Integer envType;

        private Env env;

        private String taskStatus;

        private String taskResult;

        private String deployUsername;

        private User deployUser;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;

        private String requestContent;

        private String responseContent;

        private String callbackContent;

        @Schema(description = "以前")
        private String ago;

        @Schema(description = "运行时长")
        private String runtime;

        private Boolean ticketFlag;

        @Override
        public Date getAgoTime() {
            return Objects.requireNonNullElseGet(this.startTime, Date::new);
        }

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SerDetail {

        private String serName;

        private String serMd5;

        private Long serSize;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastModified;

        private Boolean isLastHalfHour;

    }

}