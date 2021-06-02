package com.baiyi.caesar.facade.sys;

import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.param.sys.CredentialParam;
import com.baiyi.caesar.domain.vo.sys.CredentialVO;

/**
 * @Author baiyi
 * @Date 2021/5/17 3:27 下午
 * @Version 1.0
 */
public interface CredentialFacade {

    CredentialVO.Credential getCredentialById(Integer id);

    DataTable<CredentialVO.Credential> queryCredentialPage(CredentialParam.CredentialPageQuery pageQuery);

    void addCredential(CredentialVO.Credential credential);

    void updateCredential(CredentialVO.Credential credential);
}
