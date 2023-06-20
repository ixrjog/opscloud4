package com.baiyi.opscloud.domain.vo.ser;

import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;
import java.util.List;

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

        private String itemUrl;

        private String itemMd5;

        private String itemSize;

        private String deployUsername;

        private User deployUser;

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SubTask extends BaseVO {

        private Integer id;

        private Integer serDeployTaskId;

        private Integer envType;

        private Env env;

        private String taskStatus;

        private String taskResult;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startTime;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date endTime;

        private String requestContent;

        private String responseContent;
    }


}
