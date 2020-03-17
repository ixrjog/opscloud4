package com.baiyi.opscloud.account.impl;

import com.baiyi.opscloud.account.IAccount;
import com.baiyi.opscloud.account.base.AccountType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserCredential;
import com.baiyi.opscloud.domain.vo.user.OcUserCredentialVO;
import com.baiyi.opscloud.jumpserver.center.JumpserverCenter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/13 11:57 下午
 * @Version 1.0
 */
@Component("JumpserverAccount")
public class JumpserverAccount extends BaseAccount implements IAccount {

    @Resource
    private JumpserverCenter jumpserverCenter;

    @Override
    protected List<OcUser> getUserList() {
        return null;
    }

    @Override
    protected List<OcAccount> getOcAccountList() {
        return null;
    }

    @Override
    protected int getAccountType() {
        return AccountType.JUMPSEVER.getType();
    }

    /**
     * 创建
     *
     * @return
     */
    @Override
    public Boolean create(OcUser user) {
        return update(user);
    }

    /**
     * 移除
     *
     * @return
     */
    @Override
    public Boolean delete(OcUser user) {
        return jumpserverCenter.delUsersUser(user.getUsername());
    }

    @Override
    public Boolean update(OcUser user) {
        UsersUser usersUser = jumpserverCenter.saveUsersUser(user);
        if (usersUser == null)
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    @Override
    public Boolean grant(OcUser user, String resource) {
        return jumpserverCenter.grant(user,resource);
    }

    @Override
    public Boolean revoke(OcUser user, String resource) {
        return jumpserverCenter.revoke(user, resource);
    }

    @Override
    public Boolean active(OcUser user, boolean active) {
        return jumpserverCenter.activeUsersUser(user.getUsername(), active);
    }

    /**
     * 推送用户公钥 PubKey
     *
     * @param user
     * @return
     */
    @Override
    public Boolean pushSSHKey(OcUser user) {
        OcUserCredential  credential =  getOcUserSSHPubKey(user);
        if(credential == null) return Boolean.FALSE;
        return jumpserverCenter.pushKey(user,BeanCopierUtils.copyProperties(credential,OcUserCredentialVO.UserCredential.class));
    }
}
