package com.baiyi.opscloud.domain.param.ser;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
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
}
