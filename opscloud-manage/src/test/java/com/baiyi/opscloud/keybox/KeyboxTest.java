package com.baiyi.opscloud.keybox;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.util.SSHUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcKeybox;
import com.baiyi.opscloud.service.keybox.OcKeyboxService;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/3 10:55 上午
 * @Version 1.0
 */
public class KeyboxTest extends BaseUnit {

    @Resource
    private OcKeyboxService ocKeyboxService;

    @Resource
    private StringEncryptor stringEncryptor;

    @Test
    void encryptOcKeyboxPrivateKeyTest() {
        OcKeybox ocKeybox = ocKeyboxService.queryOcKeyboxById(2);
        String privateKey = ocKeybox.getPrivateKey();
        privateKey = stringEncryptor.encrypt(privateKey);
        ocKeybox.setPrivateKey(privateKey);

        // String passphrase = ocKeybox.getPassphrase();
        //  passphrase = stringEncryptor.encrypt(passphrase );
        // ocKeybox.setPassphrase(passphrase);

        ocKeyboxService.updateOcKeybox(ocKeybox);
    }


    @Test
    void decryptOcKeyboxPrivateKeyTest() {
        OcKeybox ocKeybox = ocKeyboxService.queryOcKeyboxById(1);
        String privateKey = ocKeybox.getPrivateKey();
        privateKey = stringEncryptor.decrypt(privateKey);

        System.err.println(privateKey);
    }

    @Test
    void ocKeyboxGetFingerprintTest() {
        OcKeybox ocKeybox = ocKeyboxService.queryOcKeyboxById(2);
        String fingerprint = SSHUtils.getFingerprint(ocKeybox.getPublicKey());
        System.err.println(fingerprint);
    }

}
