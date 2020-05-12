package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.domain.generator.opscloud.OcKeybox;
import com.baiyi.opscloud.facade.KeyboxFacade;
import com.baiyi.opscloud.service.keybox.OcKeyboxService;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/9 6:17 下午
 * @Version 1.0
 */
@Service
public class KeyboxFacadeImpl implements KeyboxFacade {

    @Resource
    private OcKeyboxService ocKeyboxService;

    @Resource
    private StringEncryptor stringEncryptor;

    public SSHKeyCredential getSSHKeyCredential(String systemUser) {
        OcKeybox ocKeybox = ocKeyboxService.queryOcKeyboxBySystemUser(systemUser);
        if (ocKeybox == null)
            return null;
        SSHKeyCredential key = SSHKeyCredential.builder()
                .systemUser(systemUser)
                .privateKey(stringEncryptor.decrypt(ocKeybox.getPrivateKey()))
                .publicKey(ocKeybox.getPublicKey())
                .passphrase(StringUtils.isEmpty(ocKeybox.getPassphrase()) ? "" : stringEncryptor.decrypt(ocKeybox.getPassphrase()))
                .build();
        return key;
    }

}
