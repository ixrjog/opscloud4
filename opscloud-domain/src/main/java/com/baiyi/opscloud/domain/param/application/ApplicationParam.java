package com.baiyi.opscloud.domain.param.application;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2021/7/12 1:10 下午
 * @Version 1.0
 */
public class ApplicationParam {

    @SuperBuilder(toBuilder = true)
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class ApplicationPageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "应用名称")
        private String queryName;

        @ApiModelProperty(value = "标签ID")
        private Integer tagId;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

    }

    @SuperBuilder(toBuilder = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class GetApplicationKubernetes implements IExtend {

        @ApiModelProperty(value = "应用ID")
        @Min(value = 0, message = "应用ID不能为空")
        private Integer applicationId;

        @Builder.Default
        private Boolean extend = false;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class UserPermissionApplicationPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "应用名称")
        private String queryName;

        @ApiModelProperty(value = "用户ID", example = "1")
        private Integer userId;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

    }

    @Data
    public static class Query {

        @NotNull
        @ApiModelProperty(value = "应用ID")
        private Integer applicationId;

    }

}
