package com.baiyi.opscloud.domain.param.datasource;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2021/6/15 11:19 上午
 * @Version 1.0
 */
public class DsAccountGroupParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class GroupPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "实例id")
        @NotNull
        private Integer instanceId;

        private String accountUid;

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

        @ApiModelProperty(value = "是否激活")
        private Boolean isActive;

    }
}
