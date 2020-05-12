package com.baiyi.opscloud.domain.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/9 6:20 下午
 * @Version 1.0
 */
@Data
@Builder
public class SSHKeyCredential {

    private String systemUser;
    private String passphrase;
    private String privateKey;
    private String publicKey;
}
