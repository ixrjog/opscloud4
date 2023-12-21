package com.baiyi.opscloud.facade.sys;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.sys.CredentialParam;
import com.baiyi.opscloud.domain.vo.sys.CredentialVO;

/**
 * @Author baiyi
 * @Date 2021/5/17 3:27 下午
 * @Version 1.0
 */
public interface CredentialFacade {

    DataTable<CredentialVO.Credential> queryCredentialPage(CredentialParam.CredentialPageQuery pageQuery);

    void addCredential(CredentialParam.Credential credential);

    void updateCredential(CredentialParam.Credential credential);

    void deleteCredentialById(Integer id);

}