package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.common.constants.enums.CredentialKindEnum;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.SSHUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.sys.CredentialVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.packer.secret.SecretPacker;
import com.baiyi.opscloud.packer.sys.delegate.CredentialPackerDelegate;
import com.baiyi.opscloud.service.sys.CredentialService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/17 3:49 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CredentialPacker implements IWrapper<CredentialVO.Credential> {

    private final StringEncryptor stringEncryptor;

    private final SecretPacker secretPacker;

    private final CredentialService credentialService;

    private final CredentialPackerDelegate credentialPackerDelegate;

    @Override
    public void wrap(CredentialVO.Credential credential, IExtend iExtend) {
        credentialPackerDelegate.wrap(credential, iExtend);
    }

    public void wrap(CredentialVO.ICredential iCredential) {
        if (IdUtil.isEmpty(iCredential.getCredentialId())) return;
        com.baiyi.opscloud.domain.generator.opscloud.Credential credential = credentialService.getById(iCredential.getCredentialId());
        if (credential == null) return;
        CredentialVO.Credential credentialVO = BeanCopierUtil.copyProperties(credential, CredentialVO.Credential.class);
        wrap(credentialVO);
        iCredential.setCredential(credentialVO);
    }

    public CredentialVO.Credential wrap(com.baiyi.opscloud.domain.generator.opscloud.Credential credential) {
        CredentialVO.Credential vo = BeanCopierUtil.copyProperties(credential, CredentialVO.Credential.class);
        secretPacker.wrap(vo);
        return vo;
    }

    public Credential toDO(CredentialVO.Credential vo) {
        com.baiyi.opscloud.domain.generator.opscloud.Credential credential = BeanCopierUtil.copyProperties(vo, com.baiyi.opscloud.domain.generator.opscloud.Credential.class);
        credential.setCredential(encrypt(vo.getCredential()));
        credential.setCredential2(encrypt(vo.getCredential2()));
        credential.setPassphrase(encrypt(vo.getPassphrase()));
        if (vo.getKind().equals(CredentialKindEnum.SSH_USERNAME_WITH_KEY_PAIR.getKind()) || vo.getKind().equals(CredentialKindEnum.SSH_USERNAME_WITH_PRIVATE_KEY.getKind())) {
            if (!StringUtils.isEmpty(vo.getCredential2()))
                credential.setFingerprint(SSHUtil.getFingerprint(vo.getCredential2()));
        }
        return credential;
    }

    private String encrypt(String s) {
        if (StringUtils.isEmpty(s))
            return s;
        return stringEncryptor.encrypt(s);
    }

}
