package com.baiyi.caesar.vo.server;

import com.baiyi.caesar.vo.base.BaseVO;
import com.baiyi.caesar.vo.sys.CredentialVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
    @ApiModel
    public static class Account extends BaseVO implements CredentialVO.ICredential {

        private CredentialVO.Credential credential;

        private Integer serverSize;

        private Integer id;

        private String username;

        private String displayName;

        @NotNull(message = "凭据不能为空")
        private Integer credentialId;

        @NotNull(message = "账户类型不能为空")
        private Integer accountType;

        @NotNull(message = "协议不能为空")
        private String protocol;

        private Boolean isActive;

    }
}
