package com.baiyi.caesar.service.user;

import com.baiyi.caesar.domain.generator.caesar.UserCredential;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/9 10:04 上午
 * @Version 1.0
 */
public interface UserCredentialService {

    List<UserCredential> queryByUserIdAndType(Integer userId, int credentialType);

    List<UserCredential> queryByUserId(Integer userId);

    UserCredential getById(Integer id);

    void add(UserCredential userCredential);

    void update(UserCredential userCredential);

}
