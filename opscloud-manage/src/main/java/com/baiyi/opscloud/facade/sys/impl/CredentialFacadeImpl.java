package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.sys.CredentialParam;
import com.baiyi.opscloud.domain.vo.sys.CredentialVO;
import com.baiyi.opscloud.facade.sys.CredentialFacade;
import com.baiyi.opscloud.facade.sys.impl.converter.CredentialConverter;
import com.baiyi.opscloud.factory.credential.CredentialCustomerFactory;
import com.baiyi.opscloud.factory.credential.base.ICredentialCustomer;
import com.baiyi.opscloud.packer.sys.CredentialPacker;
import com.baiyi.opscloud.service.sys.CredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/17 3:27 下午
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CredentialFacadeImpl implements CredentialFacade {

    private final CredentialService credentialService;

    private final CredentialPacker credentialPacker;

    private final CredentialConverter credentialConverter;

    @Override
    public DataTable<CredentialVO.Credential> queryCredentialPage(CredentialParam.CredentialPageQuery pageQuery) {
        DataTable<com.baiyi.opscloud.domain.generator.opscloud.Credential> table = credentialService.queryPageByParam(pageQuery);
        List<CredentialVO.Credential> data = BeanCopierUtil.copyListProperties(table.getData(), CredentialVO.Credential.class).stream()
                .peek(credentialPacker::wrap)
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void addCredential(CredentialVO.Credential credential) {
        com.baiyi.opscloud.domain.generator.opscloud.Credential pre = credentialConverter.toDO(credential);
        credentialService.add(pre);
    }

    @Override
    public void updateCredential(CredentialVO.Credential credential) {
        com.baiyi.opscloud.domain.generator.opscloud.Credential pre = credentialConverter.toDO(credential);
        credentialService.updateBySelective(pre);
    }

    @Override
    public void deleteCredentialById(Integer id) {
        com.baiyi.opscloud.domain.generator.opscloud.Credential credential = credentialService.getById(id);
        if (credential == null) return;
        Map<String, ICredentialCustomer> context = CredentialCustomerFactory.getContext();
        for (String key : context.keySet()) {
            ICredentialCustomer iCredentialCustomer = context.get(key);
            if (iCredentialCustomer.hasUsedCredential(id))
                throw new CommonRuntimeException("该凭据正在使用中！");
        }
        credentialService.deleteById(id);
    }

}
