package com.baiyi.caesar.packer.sys;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.common.util.IdUtil;
import com.baiyi.caesar.common.util.SSHUtil;
import com.baiyi.caesar.packer.base.SecretParcker;
import com.baiyi.caesar.service.sys.CredentialService;
import com.baiyi.caesar.types.CredentialKindEnum;
import com.baiyi.caesar.vo.sys.CredentialVO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/17 3:49 下午
 * @Version 1.0
 */
@Component
public class CredentialPacker extends SecretParcker {

    @Resource
    private CredentialService credentialService;

    public void wrap(CredentialVO.ICredential iCredential) {
        if (IdUtil.isEmpty(iCredential.getCredentialId())) return;
        com.baiyi.caesar.domain.generator.caesar.Credential credential = credentialService.getById(iCredential.getCredentialId());
        if (credential == null) return;
        iCredential.setCredential(toVO(credential));
    }

    private static CredentialVO.Credential toVO(com.baiyi.caesar.domain.generator.caesar.Credential credential) {
        CredentialVO.Credential vo = BeanCopierUtil.copyProperties(credential, CredentialVO.Credential.class);
        vo.setCredential("");
        vo.setCredential2("");
        vo.setPassphrase("");
        return vo;
    }

    public static List<CredentialVO.Credential> wrapVOList(List<com.baiyi.caesar.domain.generator.caesar.Credential> data) {
        return data.stream().map(CredentialPacker::toVO).collect(Collectors.toList());
    }

    public CredentialVO.Credential wrap(com.baiyi.caesar.domain.generator.caesar.Credential credential) {
        CredentialVO.Credential vo = BeanCopierUtil.copyProperties(credential, CredentialVO.Credential.class);
        super.wrap(vo);
        return vo;
    }

    public com.baiyi.caesar.domain.generator.caesar.Credential toDO(CredentialVO.Credential vo) {
        com.baiyi.caesar.domain.generator.caesar.Credential credential = BeanCopierUtil.copyProperties(vo, com.baiyi.caesar.domain.generator.caesar.Credential.class);
        if (!StringUtils.isEmpty(vo.getCredential()))
            credential.setCredential(stringEncryptor.encrypt(vo.getCredential()));
        if (!StringUtils.isEmpty(vo.getCredential2()))
            credential.setCredential2(stringEncryptor.encrypt(vo.getCredential2()));
        if (!StringUtils.isEmpty(vo.getPassphrase()))
            credential.setPassphrase(stringEncryptor.encrypt(vo.getPassphrase()));
        if (vo.getKind().equals(CredentialKindEnum.SSH_USERNAME_WITH_KEY_PAIR.getKind()) || vo.getKind().equals(CredentialKindEnum.SSH_USERNAME_WITH_PRIVATE_KEY.getKind())) {
            if (!StringUtils.isEmpty(vo.getCredential2()))
                credential.setFingerprint(SSHUtil.getFingerprint(vo.getCredential2()));
        }
        return credential;
    }

}
