package com.baiyi.opscloud.domain.param.sys;

import com.baiyi.opscloud.domain.annotation.DesensitizedField;
import com.baiyi.opscloud.domain.constants.SensitiveTypeEnum;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/5/17 5:32 下午
 * @Version 1.0
 */
public class CredentialParam {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    @Builder
    public static class Credential {

        private Integer id;
        private String title;
        private Integer kind;
        private String username;
        private String fingerprint;
        @DesensitizedField(type = SensitiveTypeEnum.PASSWORD)
        private String credential;
        @DesensitizedField(type = SensitiveTypeEnum.PASSWORD)
        private String credential2;
        @DesensitizedField(type = SensitiveTypeEnum.PASSWORD)
        private String passphrase;
        private String comment;

        // ISecret
        private String plaintext;

        private Integer quantityUsed;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class CredentialPageQuery extends PageParam {

        @Schema(description = "查询名称")
        private String queryName;

        @Schema(description = "凭据分类")
        private Integer kind;

    }

}