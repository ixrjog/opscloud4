package com.baiyi.caesar.domain.param.datasource;

import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/5/15 1:32 下午
 * @Version 1.0
 */
public class DatasourceConfigParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class DatasourceConfigPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

        @ApiModelProperty(value = "数据源类型", example = "1")
        private Integer dsType;

        @ApiModelProperty(value = "是否激活")
        private Boolean isActive;
    }
}
