package com.baiyi.opscloud.domain.param.ser;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
}
