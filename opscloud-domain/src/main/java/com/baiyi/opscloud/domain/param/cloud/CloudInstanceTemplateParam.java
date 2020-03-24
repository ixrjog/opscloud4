package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/3/20 4:39 下午
 * @Version 1.0
 */
public class CloudInstanceTemplateParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "云类型")
        private Integer cloudType;

        @ApiModelProperty(value = "查询名称")
        private String queryName;

    }

}
