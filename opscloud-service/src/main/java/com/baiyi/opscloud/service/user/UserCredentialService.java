package com.baiyi.opscloud.service.user;

import com.baiyi.opscloud.domain.generator.opscloud.UserCredential;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/9 10:04 上午
 * @Version 1.0
 */
public interface UserCredentialService {

    List<UserCredential> queryByUserIdAndType(Integer userId, int credentialType);

    int countByUserIdAndType(Integer userId, int credentialType);

    List<UserCredential> queryByUserId(Integer userId);

    UserCredential getById(Integer id);

    void add(UserCredential userCredential);

    void update(UserCredential userCredential);

    void deleteById(int id);

}