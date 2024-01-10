package com.baiyi.opscloud.domain.vo.server;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.sys.CredentialVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/25 11:27 上午
 * @Version 1.0
 */
public class ServerAccountVO {

    public interface IAccount {

        Integer getServerId();

        void setAccounts(List<Account> accounts);

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class Account extends BaseVO implements CredentialVO.ICredential, Serializable {

        @Serial
        private static final long serialVersionUID = 1815467621572374314L;

        private CredentialVO.Credential credential;

        private Integer serverSize;

        private Integer id;

        private String username;

        private String displayName;

        private Integer credentialId;

        private Integer accountType;

        private String protocol;

        private Boolean isActive;

        private String comment;

    }

}