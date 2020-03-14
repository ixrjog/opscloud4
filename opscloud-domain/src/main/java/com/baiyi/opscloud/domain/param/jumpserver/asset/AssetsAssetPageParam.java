package com.baiyi.opscloud.domain.param.jumpserver.asset;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/3/12 2:45 下午
 * @Version 1.0
 */
public class AssetsAssetPageParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "资产节点id")
        private String assetsNodeId;

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性",example = "0")
        private Integer extend;

        @ApiModelProperty(value = "资产是否有效",example = "-1")
        private Integer isActive;
    }
}
