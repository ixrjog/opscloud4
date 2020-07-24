package com.baiyi.opscloud.account;

import com.baiyi.opscloud.account.builder.UserBuilder;
import com.baiyi.opscloud.account.config.AuthConfig;
import com.baiyi.opscloud.account.factory.AccountFactory;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.param.auth.LogParam;
import com.baiyi.opscloud.domain.vo.auth.LogVO;
import com.baiyi.opscloud.facade.AuthBaseFacade;
import com.baiyi.opscloud.ldap.credential.PersonCredential;
import com.baiyi.opscloud.ldap.handler.LdapHandler;
import com.baiyi.opscloud.service.user.OcUserService;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/8 8:06 下午
 * @Version 1.0
 */
@Component("AccountCenter")
public class AccountCenter implements InitializingBean {

    @Resource
    private LdapHandler ldapHandler;

    @Resource
    private AuthBaseFacade authBaseFacade;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private StringEncryptor stringEncryptor;

    public static final String LDAP_ACCOUNT_KEY = "LdapAccount";

    @Resource
    private AuthConfig authConfig;

    /**
     * 登录接口
     *
     * @param loginParam
     * @return
     */
    public BusinessWrapper<LogVO.LoginVO> loginCheck(LogParam.LoginParam loginParam) {
        com.baiyi.opscloud.ldap.credential.PersonCredential credential = PersonCredential.builder()
                .username(loginParam.getUsername())
                .password(loginParam.getPassword())
                .build();
        BusinessWrapper<OcUser> wrapper = getLoginUser(credential);
        if (!wrapper.isSuccess()) return new BusinessWrapper<>(wrapper.getCode(), wrapper.getDesc());
        OcUser ocUser = wrapper.getBody();
        // 验证通过
        if (tryCredential(ocUser, credential)) {
            String token = UUIDUtils.getUUID();
            authBaseFacade.setUserToken(loginParam.getUsername(), token);
            LogVO.LoginVO loginVO = new LogVO.LoginVO();
            loginVO.setName(ocUser.getDisplayName());
            loginVO.setUuid(ocUser.getUuid());
            loginVO.setToken(token);
            authBaseFacade.setOcUserPassword(ocUser, loginParam.getPassword());
            initialUser(ocUser);
            return new BusinessWrapper(loginVO);
        } else {
            return new BusinessWrapper(ErrorEnum.USER_LOGIN_FAILUER);
        }
    }

    private boolean tryCredential(OcUser ocUser, PersonCredential credential) {
        if ("ldap".equals(authConfig.getExternalAuthentication()))
            return ldapHandler.loginCheck(credential);
        try {
            return stringEncryptor.decrypt(ocUser.getPassword()).equals(credential.getPassword());
        } catch (Exception ignored) {
            return false;
        }
    }


    private BusinessWrapper<OcUser> getLoginUser(PersonCredential credential) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(credential.getUsername());
        if (ocUser == null)
            return new BusinessWrapper(ErrorEnum.USER_NOT_EXIST);
        if (!ocUser.getIsActive())
            return new BusinessWrapper(ErrorEnum.ACCOUNT_IS_DISABLE);
        return new BusinessWrapper<>(ocUser);
    }

    /**
     * 登录初始化
     *
     * @param ocUser
     */
    private void initialUser(OcUser ocUser) {
        try {
            AccountFactory.getAccountByKey("JumpserverAccount").pushSSHKey(ocUser);
        } catch (Exception ignored) {
        }
    }

    public Boolean create(String key, OcUser user) {
        IAccount account = AccountFactory.getAccountByKey(key);
        return account.create(user);
    }

    public Boolean create(OcUser user) {
        Boolean result = create(LDAP_ACCOUNT_KEY, user);
        if (result) {
            Map<String, IAccount> accountContainer = AccountFactory.getAccountContainer();
            for (String key : accountContainer.keySet()) {
                if (key.equals(LDAP_ACCOUNT_KEY)) continue;
                IAccount account = accountContainer.get(key);
                if (!account.create(user))
                    return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public Boolean active(OcUser user, boolean active) {
        Map<String, IAccount> accountContainer = AccountFactory.getAccountContainer();
        for (String key : accountContainer.keySet()) {
            IAccount account = accountContainer.get(key);
            if (!account.active(user, active))
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 授权
     *
     * @param user
     * @param resource
     * @return
     */
    public Boolean grant(OcUser user, String resource) {
        Map<String, IAccount> accountContainer = AccountFactory.getAccountContainer();
        for (String key : accountContainer.keySet()) {
            if (key.equals(LDAP_ACCOUNT_KEY)) continue;
            IAccount account = accountContainer.get(key);
            if (!account.grant(user, resource))
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 撤销授权
     *
     * @param user
     * @param resource
     * @return
     */
    public Boolean revoke(OcUser user, String resource) {
        Map<String, IAccount> accountContainer = AccountFactory.getAccountContainer();
        for (String key : accountContainer.keySet()) {
            if (key.equals(LDAP_ACCOUNT_KEY)) continue;
            IAccount account = accountContainer.get(key);
            if (!account.revoke(user, resource))
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    public Boolean update(String key, OcUser user) {
        IAccount account = AccountFactory.getAccountByKey(key);
        return account.update(user);
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    public Boolean update(OcUser user) {
        Boolean result = update(LDAP_ACCOUNT_KEY, user);
        if (result) {
            Map<String, IAccount> accountContainer = AccountFactory.getAccountContainer();
            for (String key : accountContainer.keySet()) {
                if (key.equals(LDAP_ACCOUNT_KEY)) continue;
                IAccount account = accountContainer.get(key);
                account.update(user);
            }
        }
        return Boolean.TRUE;
    }

    public void pushSSHKey(OcUser user) {
        Map<String, IAccount> accountContainer = AccountFactory.getAccountContainer();
        accountContainer.keySet().forEach(k -> accountContainer.get(k).pushSSHKey(user));
    }

    public void sync(String key) {
        IAccount account = AccountFactory.getAccountByKey(key);
        account.sync();
    }

    @Override
    public void afterPropertiesSet() {
        tryInitialAdmin();
    }

    /**
     * 尝试生成管理员账户
     *
     * @param
     */
    private void tryInitialAdmin() {
        if (authConfig.getAdmin() == null) return;
        AuthConfig.Admin admin = authConfig.getAdmin();
        if (StringUtils.isEmpty(admin.getUsername())) return;
        if (StringUtils.isEmpty(admin.getPassword())) return;
        OcUser ocUser = ocUserService.queryOcUserByUsername(admin.getUsername());
        if (ocUser != null) return;
        ocUser = UserBuilder.build(admin);
        ocUserService.addOcUser(ocUser);
        authBaseFacade.setOcUserPassword(ocUser, admin.getPassword());
        authBaseFacade.authorizedAdminAllRole(ocUser); // 授权所有角色
    }

}
