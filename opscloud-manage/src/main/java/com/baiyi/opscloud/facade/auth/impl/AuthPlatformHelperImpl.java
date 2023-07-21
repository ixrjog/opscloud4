package com.baiyi.opscloud.facade.auth.impl;

import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.domain.generator.opscloud.AuthPlatform;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.param.auth.IAuthPlatform;
import com.baiyi.opscloud.facade.auth.PlatformAuthValidator;
import com.baiyi.opscloud.service.auth.AuthPlatformService;
import com.baiyi.opscloud.service.sys.CredentialService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/9/8 11:59
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AuthPlatformHelperImpl implements PlatformAuthValidator {

    private final AuthPlatformService authPlatformService;

    private final CredentialService credentialService;

    private final StringEncryptor stringEncryptor;

    @Override
    public AuthPlatform verify(IAuthPlatform iAuthPlatform) {
        AuthPlatform authPlatform = authPlatformService.getByName(iAuthPlatform.getPlatform());
        if (authPlatform == null) {
            throw new AuthenticationException("平台认证错误: 无效的平台名称！");
        }
        Credential credential = credentialService.getById(authPlatform.getCredentialId());
        if (credential == null) {
            throw new AuthenticationException("平台认证错误: 凭据不存在！");
        }
        String token = stringEncryptor.decrypt(credential.getCredential());
        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException("平台认证错误: 凭据不存在！");
        }
        if (!token.equals(iAuthPlatform.getPlatformToken())) {
            throw new AuthenticationException("平台认证错误: Token错误！");
        }
        return authPlatform;
    }

}
