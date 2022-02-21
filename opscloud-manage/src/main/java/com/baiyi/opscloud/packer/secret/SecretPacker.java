package com.baiyi.opscloud.packer.secret;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.vo.base.ISecret;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/17 3:57 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SecretPacker {

    private final StringEncryptor stringEncryptor;

    public void wrap(ISecret iSecret) {
        iSecret.setPlaintext(stringEncryptor.decrypt(iSecret.getSecret()));
    }

    public void wrap(User user) {
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(stringEncryptor.encrypt(user.getPassword()));
        }
    }

}
