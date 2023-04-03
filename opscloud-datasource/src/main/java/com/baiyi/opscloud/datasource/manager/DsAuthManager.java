package com.baiyi.opscloud.datasource.manager;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.exception.auth.AuthenticationException;
import com.baiyi.opscloud.core.factory.AuthProviderFactory;
import com.baiyi.opscloud.core.provider.auth.BaseAuthProvider;
import com.baiyi.opscloud.datasource.manager.base.BaseManager;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.model.Authorization;
import com.baiyi.opscloud.domain.param.auth.LoginParam;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 认证供应商管理类
 *
 * @Author baiyi
 * @Date 2021/6/23 1:13 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class DsAuthManager extends BaseManager {

    private final StringEncryptor stringEncryptor;

    /**
     * 支持认证的实例类型
     */
    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.LDAP};

    @Override
    protected DsTypeEnum[] getFilterInstanceTypes() {
        return FILTER_INSTANCE_TYPES;
    }

    @Override
    protected String getTag() {
        return TagConstants.AUTHORIZATION.getTag();
    }

    public boolean tryLogin(User user, LoginParam.Login loginParam) throws AuthenticationException {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) {
            return loginWithLocal(user, loginParam);
        }
        Authorization.Credential credential = Authorization.Credential.builder()
                .username(loginParam.getUsername())
                .password(loginParam.getPassword())
                .build();
        for (DatasourceInstance instance : instances) {
            BaseAuthProvider authProvider = AuthProviderFactory.getProvider(instance.getInstanceType());
            if (authProvider == null) {
                continue;
            }
            if (authProvider.login(instance, credential)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 本地认证
     *
     * @param user
     * @param loginParam
     * @return
     */
    private boolean loginWithLocal(User user, LoginParam.Login loginParam) {
        return verifyPassword(loginParam.getPassword(), user.getPassword());
    }

    /**
     * 校验密码
     *
     * @param password         密码
     * @param encryptedPassword 加密的密码
     * @return
     */
    private boolean verifyPassword(String password, String encryptedPassword) {
        return password.equals(stringEncryptor.decrypt(encryptedPassword));
    }

}
