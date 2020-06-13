package com.baiyi.opscloud.domain.param.ansible;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/4/16 2:48 下午
 * @Version 1.0
 */
public class AnsibleScriptParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "模糊查询专用")
        private String queryName;

        @ApiModelProperty(value = "使用类型", example = "0")
        private Integer useType;
    }
}
