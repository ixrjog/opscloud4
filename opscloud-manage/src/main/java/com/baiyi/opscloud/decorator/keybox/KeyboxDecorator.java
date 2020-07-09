package com.baiyi.opscloud.decorator.keybox;

import com.baiyi.opscloud.domain.vo.keybox.KeyboxVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/5/21 11:31 上午
 * @Version 1.0
 */
@Component
public class KeyboxDecorator {

//    @Resource
//    private StringEncryptor stringEncryptor;

    public KeyboxVO.Keybox decorator(KeyboxVO.Keybox keybox) {
        keybox.setPassphrase(null);
        keybox.setPublicKey(null);
        keybox.setPrivateKey(null);
        return keybox;
    }


}
