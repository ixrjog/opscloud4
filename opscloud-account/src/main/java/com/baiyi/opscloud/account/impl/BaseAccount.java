package com.baiyi.opscloud.account.impl;


import com.baiyi.opscloud.account.IAccount;
import com.baiyi.opscloud.account.factory.AccountFactory;
import com.baiyi.opscloud.common.base.CredentialType;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserCredential;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.user.OcAccountService;
import com.baiyi.opscloud.service.user.OcUserCredentialService;
import com.baiyi.opscloud.service.user.OcUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_EXECUTOR;


/**
 * @Author baiyi
 * @Date 2019/12/31 1:43 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseAccount implements InitializingBean, IAccount {

    public static final int PASSWORD_LENGTH = 16; // 初始密码长度

    @Resource
    protected OcUserService ocUserService;

    @Resource
    protected OcAccountService ocAccountService;

    @Resource
    protected OcServerGroupService ocServerGroupService;

    @Resource
    protected OcUserCredentialService ocUserCredentialService;

    protected final static boolean GRANT =true;
    protected final static boolean REVOKE =false;

    protected OcAccount getAccount(OcUser ocUser) {
        return ocAccountService.queryOcAccountByUsername(getAccountType(), ocUser.getUsername());
    }

    private void saveOcUserListByLdap(List<OcUser> ocUserList) {
        ocUserList.forEach(this::saveOcUser);
    }

    private void saveOcAccount(OcAccount preOcAccount, Map<String, OcAccount> map) {
        if (map.containsKey(preOcAccount.getAccountId())) {
            OcAccount account = map.get(preOcAccount.getAccountId());
            updateOcAccount(preOcAccount, account);
            map.remove(preOcAccount.getAccountId());
        } else {
            ocAccountService.addOcAccount(preOcAccount);
        }
    }

    /**
     * @param preOcAccount
     * @param ocAccount
     */
    protected void updateOcAccount(OcAccount preOcAccount, OcAccount ocAccount) {
        preOcAccount.setId(ocAccount.getId());
        if (!StringUtils.isEmpty(ocAccount.getPassword())) // 插入用户密码
            preOcAccount.setPassword(ocAccount.getPassword());
        ocAccountService.updateOcAccount(preOcAccount);
    }

    protected Map<String, OcAccount> getAccountMap(List<OcAccount> ocAccountList) {
        if (CollectionUtils.isEmpty(ocAccountList))
            ocAccountList = ocAccountService.queryOcAccountByAccountType(getAccountType());
        return ocAccountList.stream().collect(Collectors.toMap(OcAccount::getAccountId, a -> a, (k1, k2) -> k1));
    }

    abstract protected int getAccountType();

    /**
     * 只更新ldap源，其它源只添加条目
     *
     * @param ocUser
     * @return
     */
    private Boolean saveOcUser(OcUser ocUser) {
        try {
            if (ocUser.getId() != null && ocUser.getId() != 0) {
                if (!StringUtils.isEmpty(ocUser.getSource()) && ocUser.getSource().equals("ldap")) {
                    ocUserService.updateOcUser(ocUser);
                } else {
                    return true;
                }
            } else {
                OcUser checkUser = ocUserService.queryOcUserByUsername(ocUser.getUsername());
                if (checkUser == null) {
                    ocUserService.addOcUser(ocUser);
                } else {
                    ocUser.setId(checkUser.getId());
                    return saveOcUser(ocUser);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected OcUserCredential getOcUserSSHPubKey(OcUser ocUser) {
        List<OcUserCredential> credentials = ocUserCredentialService.queryOcUserCredentialByUserId(ocUser.getId());
        for (OcUserCredential credential : credentials)
            if (credential.getCredentialType() == CredentialType.SSH_PUB_KEY.getType())
                return credential;
        return null;
    }

    /**
     * 全量同步
     *
     * @return
     */
    @Override
    public void sync() {
        if (getKey().equals("LdapAccount")) {
            saveOcUserListByLdap(getUserList());
            return;
        }
        List<OcAccount> accountList = getOcAccountList();
        Map<String, OcAccount> map = getAccountMap(null);
        accountList.forEach(e -> saveOcAccount(e, map));
        delAccountByMap(map);
    }

    @Override
    public Boolean sync(OcUser user) {
        return true;
    }

    private void delAccountByMap(Map<String, OcAccount> accountMap) {
        if (accountMap.isEmpty()) return;
        accountMap.keySet().forEach(k -> {
            OcAccount ocAccount = accountMap.get(k);
            ocAccountService.delOcAccount(ocAccount.getId());
        });
    }


    protected abstract List<OcUser> getUserList();

    protected abstract List<OcAccount> getOcAccountList();

    /**
     * 异步任务
     *
     * @return
     */
    @Override
    @Async(value = ASYNC_POOL_TASK_EXECUTOR)
    public void async() {
        sync();
    }


    /**
     * 移除
     *
     * @return
     */
    abstract public Boolean delete(OcUser user);

    /**
     * 更新
     *
     * @return
     */
    public Boolean update(OcUser user) {
        return true;
    }


    public Boolean isServerGroupResource() {
        return false;
    }

    /**
     * 授权
     *
     * @param user
     * @param resource
     * @return
     */
    @Override
    public Boolean grant(OcUser user, String resource) {
        return Boolean.TRUE;
    }

    /**
     * 吊销
     *
     * @param user
     * @param resource
     * @return
     */
    @Override
    public Boolean revoke(OcUser user, String resource) {
        return Boolean.TRUE;
    }

    /**
     * 推送用户公钥 PubKey
     *
     * @param user
     * @return
     */
    @Override
    public Boolean pushSSHKey(OcUser user) {
        return Boolean.TRUE;
    }

    @Override
    public String getKey() {
        return this.getClass().getSimpleName();
    }

    /**
     * 查询用户的服务器组授权
     *
     * @param ocUser
     * @return
     */
    protected List<OcServerGroup> queryUserServerGroupPermission(OcUser ocUser) {
        if (ocUser.getId() == null)
            ocUser = ocUserService.queryOcUserByUsername(ocUser.getUsername());
        return ocServerGroupService.queryUserPermissionOcServerGroupByUserId(ocUser.getId());
    }


    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        AccountFactory.register(this);
    }


}
