package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.sys.CredentialVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.packer.secret.SecretPacker;
import com.baiyi.opscloud.packer.sys.delegate.CredentialPackerDelegate;
import com.baiyi.opscloud.service.sys.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/17 3:49 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CredentialPacker implements IWrapper<CredentialVO.Credential> {

    private final SecretPacker secretPacker;

    private final CredentialService credentialService;

    private final CredentialPackerDelegate credentialPackerDelegate;

    @Override
    public void wrap(CredentialVO.Credential credential, IExtend iExtend) {
        credentialPackerDelegate.wrap(credential, iExtend);
    }

    public void wrap(CredentialVO.ICredential iCredential) {
        if (IdUtil.isEmpty(iCredential.getCredentialId())) {
            return;
        }
        com.baiyi.opscloud.domain.generator.opscloud.Credential credential = credentialService.getById(iCredential.getCredentialId());
        if (credential == null) {
            return;
        }
        CredentialVO.Credential credentialVO = BeanCopierUtil.copyProperties(credential, CredentialVO.Credential.class);
        secretPacker.wrap(credentialVO);
        iCredential.setCredential(credentialVO);
    }

}
