package com.baiyi.caesar.packer.base;

import com.baiyi.caesar.domain.vo.base.ISecret;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/17 3:57 下午
 * @Version 1.0
 */
@Component
public class SecretParcker {

    @Resource
    protected StringEncryptor stringEncryptor;

    protected String encrypt(String str){
        return stringEncryptor.encrypt(str);
    }

    private String decrypt(String str){
        return stringEncryptor.decrypt(str);
    }

    public void wrap(ISecret iSecret) {
        iSecret.setPlaintext(stringEncryptor.decrypt(iSecret.getSecret()));
    }
}
