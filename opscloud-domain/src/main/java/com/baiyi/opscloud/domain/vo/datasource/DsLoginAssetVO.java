package com.baiyi.opscloud.domain.vo.datasource;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/3/31 10:24
 * @Version 1.0
 */
public class DsLoginAssetVO {

    @Builder
    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public static class LoginAsset {

        @ApiModelProperty(value = "资产ID")
        private String assetId;

        @ApiModelProperty(value = "显示名")
        private String displayName;

        @ApiModelProperty(value = "登录IP")
        private String loginIp;

        @ApiModelProperty(value = "资产类型")
        private String assetType;

    }

}
