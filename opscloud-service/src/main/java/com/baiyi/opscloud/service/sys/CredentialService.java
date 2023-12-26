package com.baiyi.opscloud.service.sys;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.param.sys.CredentialParam;

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

    void deleteById(Integer id);

    DataTable<Credential> queryPageByParam(CredentialParam.CredentialPageQuery pageQuery);

}