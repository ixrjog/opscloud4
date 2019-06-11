package com.sdg.cmdb.plugin.ldap;

import com.sdg.cmdb.domain.ldap.LdapDO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.transaction.compensating.manager.TransactionAwareContextSourceProxy;
import org.springframework.stereotype.Service;


/**
 * Created by zxxiao on 2017/6/23.
 */
@Service
public class LDAPFactory {

    @Value(value = "${ldap.url}")
    private String ldapUrl;

    @Value(value = "${ldap.manager.passwd}")
    private String ldapManagePasswd;

    @Value(value = "${ldap.manager.dn}")
    private String manageDn;


    private LdapTemplate ldapTemplate;

    /**
     * 获取指定类型的ldapTemplate实例
     * @return
     */
    public LdapTemplate getLdapTemplateInstance() {
           return ldapTemplate;
    }

    /**
     * 构建ldapTemplate实例
     *
     * @return
     */
    public LdapTemplate buildLdapTemplate() {
        if (ldapTemplate != null) return ldapTemplate;
        LdapContextSource contextSource = buildLdapContextSource(new LdapDO(ldapUrl,manageDn,ldapManagePasswd));
        TransactionAwareContextSourceProxy sourceProxy = buildTransactionAwareContextSourceProxy(contextSource);
        ldapTemplate = new LdapTemplate(sourceProxy);
        return ldapTemplate;
    }

    private static LdapContextSource buildLdapContextSource(LdapDO ldapDO) {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapDO.getUrl());
        contextSource.setBase(ldapDO.getBase());
        contextSource.setUserDn(ldapDO.getUserDn());
        contextSource.setPassword(ldapDO.getPassword());
        contextSource.setPooled(true);
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    private static TransactionAwareContextSourceProxy buildTransactionAwareContextSourceProxy(LdapContextSource contextSource) {
        TransactionAwareContextSourceProxy sourceProxy = new TransactionAwareContextSourceProxy(contextSource);
        return sourceProxy;
    }

}
