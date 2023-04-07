package com.baiyi.opscloud.domain.param.datasource;

import com.baiyi.opscloud.domain.param.IExtend;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/5/18 4:56 下午
 * @Version 1.0
 */
public class DsInstanceParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DsInstanceQuery implements IExtend {

        @Schema(name = "数据源类型")
        private String instanceType;

        @Schema(name = "有效")
        @Builder.Default
        private Boolean isActive = true;

        private Boolean extend;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class RegisterDsInstance {

        private Integer id;

        @Schema(name = "数据源实例名称")
        @Valid
        private String instanceName;

        private String uuid;

        @Schema(name = "数据源实例分类")
        private String kind;

        @Schema(name = "数据源实例类型")
        @Valid
        private String instanceType;

        @Schema(name = "数据源配置id", example = "1")
        private Integer configId;

        @Schema(name = "父实例id", example = "1")
        private Integer parentId;

        @Schema(name = "有效")
        private Boolean isActive;

        @Schema(name = "描述")
        private String comment;

    }

}
