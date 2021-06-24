package com.baiyi.caesar.facade.user;

import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.vo.user.UserCredentialVO;

/**
 * @Author baiyi
 * @Date 2021/6/9 3:35 下午
 * @Version 1.0
 */
public interface UserCredentialFacade {

    void saveUserCredential(UserCredentialVO.Credential credential);

    /**
     * 内部使用
     * @param credential
     * @param user
     */
    void saveUserCredential(UserCredentialVO.Credential credential, User user);

}
