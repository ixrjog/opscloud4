package com.sdg.cmdb.plugin.ldap;

import com.sdg.cmdb.domain.ldap.LdapDO;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.transaction.compensating.manager.TransactionAwareContextSourceProxy;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zxxiao on 2017/6/23.
 */
@Service
public class LDAPFactory {


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
     * @param ldapDO
     * @return
     */
    public LdapTemplate buildLdapTemplate(LdapDO ldapDO) {
        if (ldapTemplate != null) return ldapTemplate;
        LdapContextSource contextSource = buildLdapContextSource(ldapDO);
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
