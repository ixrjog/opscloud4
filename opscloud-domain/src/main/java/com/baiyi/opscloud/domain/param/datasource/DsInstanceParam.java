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
    @NoArgsConstructor
    @Schema
    public static class AddDsConfig  {

        private Integer id;
        private String name;
        private Integer dsType;
        private String version;
        private String kind;
        private Boolean isActive;
        private Integer credentialId;
        private String dsUrl;
        private String propsYml;
        private String comment;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UpdateDsConfig  {

        private Integer id;
        private String name;
        private String uuid;
        private Integer dsType;
        private String version;
        private String kind;
        private Boolean isActive;
        private Integer credentialId;
        private String dsUrl;
        private String propsYml;
        private String comment;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DsInstanceQuery implements IExtend {

        @Schema(description = "数据源类型")
        private String instanceType;

        @Schema(description = "有效")
        @Builder.Default
        private Boolean isActive = true;

        private Boolean extend;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class RegisterDsInstance {

        private Integer id;

        @Schema(description = "数据源实例名称")
        @Valid
        private String instanceName;

        private String uuid;

        @Schema(description = "数据源实例分类")
        private String kind;

        @Schema(description = "数据源实例类型")
        @Valid
        private String instanceType;

        @Schema(description = "数据源实例版本")
        private String version;

        @Schema(description = "数据源配置ID", example = "1")
        private Integer configId;

        @Schema(description = "父实例ID", example = "1")
        private Integer parentId;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "描述")
        private String comment;

    }

}