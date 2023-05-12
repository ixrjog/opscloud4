package com.baiyi.opscloud.domain.param.project;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author 修远
 * @Date 2023/5/12 5:37 PM
 * @Since 1.0
 */
public class ProjectParam {

    @SuperBuilder(toBuilder = true)
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ProjectPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "项目名称")
        private String queryName;

        @Schema(description = "标签ID")
        private Integer tagId;

        @Schema(description = "展开")
        private Boolean extend;

    }
}
