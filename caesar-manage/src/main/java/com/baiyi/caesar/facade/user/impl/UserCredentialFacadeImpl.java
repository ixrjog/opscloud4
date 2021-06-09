package com.baiyi.caesar.facade.user.impl;

import com.baiyi.caesar.common.type.UserCredentialTypeEnum;
import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IdUtil;
import com.baiyi.caesar.common.util.SSHUtil;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.generator.caesar.UserCredential;
import com.baiyi.caesar.domain.vo.user.UserCredentialVO;
import com.baiyi.caesar.facade.user.UserCredentialFacade;
import com.baiyi.caesar.service.user.UserCredentialService;
import com.baiyi.caesar.service.user.UserService;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/9 3:35 下午
 * @Version 1.0
 */
@Service
public class UserCredentialFacadeImpl implements UserCredentialFacade {

    @Resource
    private UserCredentialService userCredentialService;

    @Resource
    private UserService userService;

    @Override
    public void saveUserCredential(UserCredentialVO.Credential credential) {
        User user = userService.getByUsername(SessionUtil.getUsername());
        if (user == null) return;
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
