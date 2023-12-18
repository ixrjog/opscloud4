package com.baiyi.opscloud.domain.vo.sys;

import com.baiyi.opscloud.domain.annotation.DesensitizedField;
import com.baiyi.opscloud.domain.constants.SensitiveTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ISecret;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/5/17 3:40 下午
 * @Version 1.0
 */
public class CredentialVO {

    public interface ICredential {
        Integer getCredentialId();
        void setCredential(Credential credential);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    @Builder
    public static class Credential extends BaseVO implements ISecret, Serializable {

        @Serial
        private static final long serialVersionUID = -8663845116665600709L;
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

        @Override
        public String getSecret() {
            return credential;
        }

    }

}