package com.baiyi.opscloud.domain.vo.datasource;

import io.swagger.v3.oas.annotations.media.Schema;
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

        @Schema(name = "资产ID")
        private String assetId;

        @Schema(name = "显示名")
        private String displayName;

        @Schema(name = "登录IP")
        private String loginIp;

        @Schema(name = "资产类型")
        private String assetType;

    }

}
