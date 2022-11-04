package com.baiyi.opscloud.domain.param.leo;

import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2022/11/4 14:42
 * @Version 1.0
 */
public class LeoJobParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel
    public static class JobPageQuery extends SuperPageParam implements IExtend {

        @ApiModelProperty(value = "名称")
        private String queryName;

        @ApiModelProperty(value = "应用ID")
        private Integer applicationId;

        @ApiModelProperty(value = "模板ID")
        private Integer templateId;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "标签ID")
        private Integer tagId;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        private final Integer businessType = BusinessTypeEnum.LEO_JOB.getType();

        private Boolean extend;

    }
}
