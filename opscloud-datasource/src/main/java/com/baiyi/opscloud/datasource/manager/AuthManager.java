package com.baiyi.opscloud.datasource.manager;

import com.baiyi.opscloud.common.exception.auth.AuthRuntimeException;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.factory.AuthProviderFactory;
import com.baiyi.opscloud.datasource.manager.base.BaseManager;
import com.baiyi.opscloud.datasource.provider.auth.BaseAuthProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.model.Authorization;
import com.baiyi.opscloud.domain.param.auth.LoginParam;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 认证供应商管理类
 *
 * @Author baiyi
 * @Date 2021/6/23 1:13 下午
 * @Version 1.0
 */
@Component
public class AuthManager extends BaseManager {

    @Resource
    private StringEncryptor stringEncryptor;


    private static final String AUTHORIZATION_TAG = "Authorization";

    /**
     * 支持认证的实例类型
     */
    private static final DsTypeEnum[] authorizationInstanceTypes = {DsTypeEnum.LDAP};

    @Override
    protected DsTypeEnum[] getDsTypes() {
        return authorizationInstanceTypes;
    }

    @Override
    protected String getTag(){
        return AUTHORIZATION_TAG;
    }

    public boolean tryLogin(User user, LoginParam.Login loginParam) throws AuthRuntimeException {
        List<DatasourceInstance> instances = listInstance();
        if (!CollectionUtils.isEmpty(instances)) {
            Authorization.Credential credential = Authorization.Credential.builder()
                    .username(loginParam.getUsername())
                    .password(loginParam.getPassword())
                    .build();
            for (DatasourceInstance instance : instances) {
                BaseAuthProvider authProvider = AuthProviderFactory.getProvider(instance.getInstanceType());
                if (authProvider == null) continue;
                if (authProvider.login(instance, credential)) return true;
            }
            return false; // 有认证实例不允许本地认证
        }
        return localLogin(user, loginParam);
    }

    /**
     * 本地认证
     *
     * @param user
     * @param loginParam
     * @return
     */
    private boolean localLogin(User user, LoginParam.Login loginParam) {
        return verifyPassword(loginParam.getPassword(), user.getPassword());
    }

    /**
     * 校验密码
     *
     * @param password         密码
     * @param encryptdPassword 加密的密码
     * @return
     */
    private boolean verifyPassword(String password, String encryptdPassword) {
        return password.equals(stringEncryptor.decrypt(encryptdPassword));
    }

}
