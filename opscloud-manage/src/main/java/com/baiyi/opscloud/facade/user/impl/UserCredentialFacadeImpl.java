package com.baiyi.opscloud.facade.user.impl;

import com.baiyi.opscloud.common.constants.enums.UserCredentialTypeEnum;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.SSHUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserCredential;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import com.baiyi.opscloud.facade.user.UserCredentialFacade;
import com.baiyi.opscloud.service.user.UserCredentialService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/9 3:35 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserCredentialFacadeImpl implements UserCredentialFacade {

    private final UserCredentialService userCredentialService;

    private final UserService userService;

    @Override
    public void saveUserCredential(UserCredentialVO.Credential credential) {
        User user = userService.getByUsername(SessionUtil.getUsername());
        if (user == null) return;
        saveUserCredential(credential, user);
    }

    @Override
    public void saveUserCredential(UserCredentialVO.Credential credential, User user) {
        credential.setUserId(user.getId());
        if (credential.getCredentialType() == UserCredentialTypeEnum.PUB_KEY.getType()) {
            savePubKey(credential);
        }
    }

    private void savePubKey(UserCredentialVO.Credential credential) {
        List<String> result = Splitter.on(" ").splitToList(credential.getCredential());
        credential.setFingerprint(SSHUtil.getFingerprint(Joiner.on(" ").join(result.get(0), result.get(1)))); //计算指纹
        if (result.size() == 3)
            credential.setTitle(result.get(2));
        UserCredential userCredential = BeanCopierUtil.copyProperties(credential, UserCredential.class);
        if (IdUtil.isEmpty(userCredential.getId())) {
            userCredentialService.add(userCredential);
        } else {
            userCredentialService.update(userCredential);
        }
    }

}
