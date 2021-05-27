package com.baiyi.caesar.service.sys;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.Credential;
import com.baiyi.caesar.domain.param.sys.CredentialParam;

/**
 * @Author baiyi
 * @Date 2021/5/17 3:35 下午
 * @Version 1.0
 */
public interface CredentialService {

    void add(Credential credential);

    void updateBySelective(Credential credential);

    void update(Credential credential);

    Credential getById(Integer id);

    DataTable<Credential> queryPageByParam(CredentialParam.CredentialPageQuery pageQuery);
}
