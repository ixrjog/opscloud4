package com.baiyi.opscloud.datasource.manager;

import com.baiyi.opscloud.common.exception.auth.AuthRuntimeException;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.provider.auth.BaseAuthProvider;
import com.baiyi.opscloud.datasource.factory.AuthProviderFactory;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.model.Authorization;
import com.baiyi.opscloud.domain.param.auth.LoginParam;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.tag.BaseTagService;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.google.common.collect.Lists;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证供应商管理类
 * @Author baiyi
 * @Date 2021/6/23 1:13 下午
 * @Version 1.0
 */
@Component
public class AuthProviderManager {

    @Resource
    private BaseTagService baseTagService;

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private StringEncryptor stringEncryptor;

    protected static final int DsInstanceBusinessType = BusinessTypeEnum.DATASOURCE_INSTANCE.getType();

    private static final String TAG_AUTHORIZATION = "Authorization";

    /**
     * 支持认证的实例类型
     */
    private static final DsTypeEnum[] authorizationInstanceTypes = {DsTypeEnum.LDAP};

    /**
     * 查询所有可认证的实例
     *
     * @return
     */
    private List<DatasourceInstance> listAuthorizationInstance() {
        List<DatasourceInstance> instances = Lists.newArrayList();
        for (DsTypeEnum typeEnum : authorizationInstanceTypes) {
            DsInstanceParam.DsInstanceQuery query = DsInstanceParam.DsInstanceQuery.builder()
                    .instanceType(typeEnum.getName())
                    .build();
            // 过滤掉没有 Authorization 标签的实例
            instances.addAll(
                    dsInstanceService.queryByParam(query).stream().filter(e ->
                            baseTagService.hasBusinessTag(TAG_AUTHORIZATION, DsInstanceBusinessType, e.getId(), true)
                    ).collect(Collectors.toList())
            );
        }
        return instances;
    }

    public boolean tryLogin(User user, LoginParam.Login loginParam) throws AuthRuntimeException {
        List<DatasourceInstance> instances = listAuthorizationInstance();
        if (CollectionUtils.isEmpty(instances))
            throw new AuthRuntimeException(ErrorEnum.AUTH_THERE_ARE_NO_AUTHENTICATED_INSTANCES_FAILUER);
        Authorization.Credential credential = Authorization.Credential.builder()
                .username(loginParam.getUsername())
                .password(loginParam.getPassword())
                .build();
        for (DatasourceInstance instance : instances) {
            BaseAuthProvider authProvider = AuthProviderFactory.getProvider(instance.getInstanceType());
            if (authProvider == null) continue;
            if (authProvider.login(instance, credential)) return true;
        }
        return localLogin(user,loginParam);
    }

    /**
     * 本地认证
     * @param user
     * @param loginParam
     * @return
     */
    private boolean localLogin(User user,LoginParam.Login loginParam){
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
