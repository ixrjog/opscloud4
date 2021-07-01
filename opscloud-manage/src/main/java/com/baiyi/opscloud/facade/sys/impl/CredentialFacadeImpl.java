package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.sys.CredentialParam;
import com.baiyi.opscloud.facade.sys.CredentialFacade;
import com.baiyi.opscloud.packer.sys.CredentialPacker;
import com.baiyi.opscloud.service.sys.CredentialService;
import com.baiyi.opscloud.domain.vo.sys.CredentialVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/17 3:27 下午
 * @Version 1.0
 */
@Service
@Slf4j
public class CredentialFacadeImpl implements CredentialFacade {

    @Resource
    private CredentialService credentialService;

    @Resource
    private CredentialPacker credentialPacker;

    @Override
    public CredentialVO.Credential getCredentialById(Integer id) {
        com.baiyi.opscloud.domain.generator.opscloud.Credential credential = credentialService.getById(id);
        return credentialPacker.wrap(credential);
    }

    @Override
    public DataTable<CredentialVO.Credential> queryCredentialPage(CredentialParam.CredentialPageQuery pageQuery) {
        DataTable<com.baiyi.opscloud.domain.generator.opscloud.Credential> table = credentialService.queryPageByParam(pageQuery);
        return new DataTable<>(CredentialPacker.wrapVOList(table.getData()), table.getTotalNum());
    }

    @Override
    public void addCredential(CredentialVO.Credential credential) {
        com.baiyi.opscloud.domain.generator.opscloud.Credential pre = credentialPacker.toDO(credential);
        credentialService.add(pre);
    }

    @Override
    public void updateCredential(CredentialVO.Credential credential) {
        com.baiyi.opscloud.domain.generator.opscloud.Credential pre = credentialPacker.toDO(credential);
        credentialService.updateBySelective(pre);
    }

}
