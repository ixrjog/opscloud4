package com.baiyi.opscloud.domain.param.jumpserver.assetsNode;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/3/13 1:14 下午
 * @Version 1.0
 */
public class AssetsNodePageParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "节点名称")
        private String value;

    }
}
