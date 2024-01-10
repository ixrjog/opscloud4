package com.baiyi.opscloud.domain.param.ser;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import com.baiyi.opscloud.domain.param.auth.IAuthPlatform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

/**
 * @Author 修远
 * @Date 2023/6/9 2:49 PM
 * @Since 1.0
 */
public class SerDeployParam {

    @SuperBuilder(toBuilder = true)
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class TaskPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "查询名称")
        private String queryName;

        @NotNull
        @Schema(description = "应用id")
        private Integer applicationId;

        @Schema(description = "是否有效")
        private Boolean isActive;

        @Schema(description = "是否完成")
        private Boolean isFinish;

        @Schema(description = "展开")
        private Boolean extend;

    }

    @Data
    @Schema
    public static class QueryByUuid {

        @Schema(description = "任务UUID")
        @NotBlank(message = "任务UUID不能为空")
        private String taskUuid;

    }

    @Data
    @Schema
    public static class AddTask {

        @Schema(description = "应用ID")
        @NotNull(message = "应用不能为空")
        private Integer applicationId;

        @Schema(description = "任务名称")
        @NotBlank(message = "任务名称不能为空")
        private String taskName;

        @Schema(description = "任务描述")
        @NotBlank(message = "任务描述不能为空")
        @Length(max = 128, message = "任务描述长度大于128")
        private String taskDesc;

        @Schema(description = "有效")
        @NotNull(message = "是否有效不能为空")
        private Boolean isActive;

        @Schema(description = "是否已完成任务")
        @NotNull(message = "是否完成任务不能为空")
        private Boolean isFinish;

    }

    @Data
    @Schema
    public static class UpdateTask {

        @NotNull(message = "任务id不能为空")
        private Integer id;

        @Schema(description = "应用ID")
        @NotNull(message = "应用不能为空")
        private Integer applicationId;

        @Schema(description = "任务名称")
        @NotBlank(message = "任务名称不能为空")
        private String taskName;

        @Schema(description = "任务描述")
        @NotBlank(message = "任务名称不能为空")
        @Length(max = 128, message = "任务描述长度大于128")
        private String taskDesc;

        @Schema(description = "有效")
        @NotNull(message = "是否有效不能为空")
        private Boolean isActive;

        @Schema(description = "是否已完成任务")
        @NotNull(message = "是否完成任务不能为空")
        private Boolean isFinish;

    }

    @Data
    @Schema
    public static class AddSubTask {

        @Schema(description = "任务ID")
        @NotNull(message = "任务ID不能为空")
        private Integer serDeployTaskId;

        @Schema(description = "应用ID")
        @NotNull(message = "应用不能为空")
        private Integer applicationId;

        @Schema(description = "环境类型")
        @NotNull(message = "环境类型不能为空")
        private Integer envType;

    }

    @Data
    @Schema
    public static class DeploySubTask {

        @Schema(description = "子任务ID")
        @NotNull(message = "子任务ID不能为空")
        private Integer serDeploySubTaskId;

    }

    @Data
    @Schema
    public static class DeploySubTaskCallback implements IAuthPlatform {

        @NotBlank(message = "平台名称不能为空")
        @Schema(description = "平台名称(用于审计)")
        public String platform;

        @NotBlank(message = "平台令牌不能为空")
        @Schema(description = "平台令牌用于鉴权")
        public String platformToken;

        @Schema(description = "子任务ID")
        @NotNull(message = "子任务ID不能为空")
        private Integer serDeploySubTaskId;

        @Schema(description = "回调内容")
        @NotBlank(message = "回调内容不能为空")
        private String content;

    }

    @Data
    @Schema
    public static class QueryCurrentSer {

        @Schema(description = "应用名称")
        @NotBlank(message = "应用名称为空")
        private String applicationName;

        @Schema(description = "环境名称")
        @NotBlank(message = "环境名称不能为空")
        private String envName;

    }

}