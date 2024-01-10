package com.baiyi.opscloud.domain.param.auth;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2020/2/15 5:20 下午
 * @Version 1.0
 */
public class AuthGroupParam {

    @Data
    @Schema
    public static class Group {

        @Schema(description = "资源数量")
        private Integer resourceSize;

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "资源组名称")
        private String groupName;

        @Schema(description = "基本路径")
        private String basePath;

        @Schema(description = "资源描述")
        private String comment;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AuthGroupPageQuery extends SuperPageParam implements IExtend {

        @Schema(description = "资源组名称")
        private String groupName;

        @Schema(description = "展开")
        private Boolean extend;

    }

}