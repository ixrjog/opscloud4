package com.baiyi.caesar.domain.bo;

import com.baiyi.caesar.domain.generator.caesar.Credential;
import com.baiyi.caesar.domain.generator.caesar.ServerAccount;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/9 6:20 下午
 * @Version 1.0
 */
@Data
@Builder
public class SshCredential {
//    private String systemUser;
//    private String passphrase;
//    private String privateKey;
//    private String publicKey;

    private Credential credential;

    private ServerAccount serverAccount;
}
