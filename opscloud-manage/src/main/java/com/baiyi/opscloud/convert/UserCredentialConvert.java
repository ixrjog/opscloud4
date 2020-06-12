package com.baiyi.opscloud.convert;

import com.baiyi.opscloud.common.base.CredentialType;
import com.baiyi.opscloud.common.util.SSHUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserCredential;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/2/27 6:27 下午
 * @Version 1.0
 */
public class UserCredentialConvert {


    public static OcUserCredential convertOcUserCredential(UserCredentialVO.UserCredential userCredential) {
        OcUserCredential ocUserCredential = new OcUserCredential();

        ocUserCredential.setCredentialType(userCredential.getCredentialType());
        ocUserCredential.setUserId(userCredential.getUserId());
        ocUserCredential.setUsername(userCredential.getUsername());
        ocUserCredential.setCredential(userCredential.getCredential());
        // 拆分PubKey Title
        if (userCredential.getCredentialType() == CredentialType.SSH_PUB_KEY.getType() && !StringUtils.isEmpty(userCredential.getCredential())) {
            ocUserCredential.setFingerprint(SSHUtils.getFingerprint(userCredential.getCredential())); // 计算指纹
            String credential = userCredential.getCredential();
            String[] content = credential.split(" +");
            if (content.length == 3)
                ocUserCredential.setTitle(content[2]);
        }
        return ocUserCredential;
    }

}
