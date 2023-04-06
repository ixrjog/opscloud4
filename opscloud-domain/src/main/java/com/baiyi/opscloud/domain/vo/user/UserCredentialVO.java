package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/2/27 1:17 下午
 * @Version 1.0
 */
public class UserCredentialVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class CredentialDetails {
        private Map<String, List<Credential>> credentialMap; // 用户凭据
        private Map<String,List<DsAssetVO.Asset>> assetCredentialMap; // 资产凭据
    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @Schema
    public static class Credential extends BaseVO {
        @Schema(name = "主键")
        private Integer id;

        @Schema(name = "用户ID")
        private Integer userId;

        @Schema(name = "实例UUID")
        private String instanceUuid;

        @Schema(name = "标题")
        private String title;

        @Schema(name = "凭据类型")
        private Integer credentialType;

        @Schema(name = "凭据内容")
        @NotNull(message = "凭据不能为空")
        private String credential;

        @Schema(name = "凭据指纹")
        private String fingerprint;

        @Schema(name = "有效")
        private Boolean valid;

        @Schema(name = "有效期")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;

        private String comment;
    }

}
