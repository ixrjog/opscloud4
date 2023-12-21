package com.baiyi.opscloud.common.util;

import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/31 3:55 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CredentialUtil {

    private final StringEncryptor stringEncryptor;

    public void decrypt(Credential credential) {
        // 解密
        if (!StringUtils.isEmpty(credential.getCredential())) {
            credential.setCredential(stringEncryptor.decrypt(credential.getCredential()));
        }
        if (!StringUtils.isEmpty(credential.getCredential2())) {
            credential.setCredential2(stringEncryptor.decrypt(credential.getCredential2()));
        }
        if (!StringUtils.isEmpty(credential.getPassphrase())) {
            credential.setPassphrase(stringEncryptor.decrypt(credential.getPassphrase()));
        }
    }

}