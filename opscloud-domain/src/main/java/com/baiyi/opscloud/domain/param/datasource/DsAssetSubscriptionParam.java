package com.baiyi.opscloud.domain.param.datasource;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/8/27 4:00 下午
 * @Version 1.0
 */
public class DsAssetSubscriptionParam {

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class AssetSubscriptionPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "展开")
        private Boolean extend;

        @ApiModelProperty(value = "是否有效")
        @Builder.Default
        private Boolean isActive = true;

    }
}
