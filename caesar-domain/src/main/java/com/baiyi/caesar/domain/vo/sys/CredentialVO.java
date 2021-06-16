package com.baiyi.caesar.domain.vo.sys;

import com.baiyi.caesar.domain.vo.base.BaseVO;
import com.baiyi.caesar.domain.vo.base.ISecret;
import io.swagger.annotations.ApiModel;
import lombok.*;

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
    @ApiModel
    @Builder
    public static class Credential extends BaseVO implements ISecret , Serializable {

        private static final long serialVersionUID = -8663845116665600709L;
        private Integer id;
        private String title;
        private Integer kind;
        private String username;
        private String fingerprint;
        private String credential;
        private String credential2;
        private String passphrase;
        private String comment;

        // ISecret
        private String plaintext;

        @Override
        public String getSecret() {
            return credential;
        }

    }
}
