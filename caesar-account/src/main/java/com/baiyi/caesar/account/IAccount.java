package com.baiyi.caesar.account;

import com.baiyi.caesar.domain.generator.caesar.User;
import com.baiyi.caesar.domain.model.Authorization;
import com.baiyi.caesar.domain.vo.datasource.DsInstanceVO;

/**
 * @Author baiyi
 * @Date 2021/6/10 10:27 上午
 * @Version 1.0
 */
public interface IAccount {

    void pullAccount(DsInstanceVO.Instance dsInstance);

    void pullAccountGroup(DsInstanceVO.Instance dsInstance);

    String getKey();

    boolean authentication(DsInstanceVO.Instance dsInstance, Authorization.Credential credential);

    void update(DsInstanceVO.Instance dsInstance, User user);

    void create(DsInstanceVO.Instance dsInstance, User user);

    void delete(DsInstanceVO.Instance dsInstance, User user);
}
