package com.baiyi.opscloud.domain.param.datasource;

import com.baiyi.opscloud.domain.param.IExtend;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

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
    @ApiModel
    public static class DsInstanceQuery implements IExtend {

        @ApiModelProperty(value = "数据源类型")
        private String instanceType;

        @ApiModelProperty(value = "有效")
        @Builder.Default
        private Boolean isActive = true;

        private Boolean extend;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class RegisterDsInstance {

        private Integer id;

        @ApiModelProperty(value = "数据源实例名称")
        @Valid
        private String instanceName;

        private String uuid;

        @ApiModelProperty(value = "数据源实例分类")
        private String kind;

        @ApiModelProperty(value = "数据源实例类型")
        @Valid
        private String instanceType;

        @ApiModelProperty(value = "数据源配置id", example = "1")
        private Integer configId;

        @ApiModelProperty(value = "父实例id", example = "1")
        private Integer parentId;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "描述")
        private String comment;

    }


}
