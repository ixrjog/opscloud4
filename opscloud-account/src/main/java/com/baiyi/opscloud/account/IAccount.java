package com.baiyi.opscloud.account;

import com.baiyi.opscloud.domain.generator.opscloud.OcUser;

/**
 * @Author baiyi
 * @Date 2020/1/10 4:38 下午
 * @Version 1.0
 */
public interface IAccount {

    /**
     * 同步账户
     * LDAP 为远端到本地
     * 其它是本地到远端
     *
     * @return
     */
    void sync();

    Boolean sync(OcUser user);

    /**
     * 创建账户
     *
     * @param user
     * @return
     */
    Boolean create(OcUser user);

    void async();

    /**
     * 是否激活
     *
     * @param user
     * @param active
     * @return
     */
    Boolean active(OcUser user, boolean active);

    Boolean delete(OcUser user);

    Boolean update(OcUser user);

    String getKey();

    /**
     * 授权
     *
     * @param user
     * @param resource
     * @return
     */
    Boolean grant(OcUser user, String resource);

    /**
     * 撤销授权
     *
     * @param user
     * @param resource
     * @return
     */
    Boolean revoke(OcUser user, String resource);

    /**
     * 推送用户公钥 PubKey
     *
     * @param user
     * @return
     */
    Boolean pushSSHKey(OcUser user);

}
