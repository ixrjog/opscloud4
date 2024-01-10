package com.baiyi.opscloud.facade.sys.impl.converter;

import com.baiyi.opscloud.common.constants.enums.CredentialKindEnum;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.SSHUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.param.sys.CredentialParam;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/2/24 10:55 AM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CredentialConverter {

    private final StringEncryptor stringEncryptor;

    public Credential to(CredentialParam.Credential vo) {
        com.baiyi.opscloud.domain.generator.opscloud.Credential credential = BeanCopierUtil.copyProperties(vo, com.baiyi.opscloud.domain.generator.opscloud.Credential.class);
        credential.setCredential(encrypt(vo.getCredential()));
        credential.setCredential2(encrypt(vo.getCredential2()));
        credential.setPassphrase(encrypt(vo.getPassphrase()));
        if (vo.getKind().equals(CredentialKindEnum.SSH_USERNAME_WITH_KEY_PAIR.getKind()) || vo.getKind().equals(CredentialKindEnum.SSH_USERNAME_WITH_PRIVATE_KEY.getKind())) {
            if (!StringUtils.isEmpty(vo.getCredential2())) {
                credential.setFingerprint(SSHUtil.getFingerprint(vo.getCredential2()));
            }
        }
        return credential;
    }

    private String encrypt(String s) {
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        return stringEncryptor.encrypt(s);
    }

}