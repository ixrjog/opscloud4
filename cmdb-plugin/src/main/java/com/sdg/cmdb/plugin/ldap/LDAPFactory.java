package com.sdg.cmdb.plugin.ldap;

import com.sdg.cmdb.domain.ldap.LdapDO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.transaction.compensating.manager.TransactionAwareContextSourceProxy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


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

    @Value(value = "${ldap.slave.url}")
    private String ldapSlaveUrl;

    // master
    private LdapTemplate ldapTemplate;

    // slave
    private List<LdapTemplate> ldapTemplateSlaveList;

    /**
     * 获取指定类型的ldapTemplate实例
     *
     * @return
     */
    public LdapTemplate getLdapTemplateInstance() {
        return ldapTemplate;
    }

    public List<LdapTemplate> getLdapTemplateSlaveInstance() {
        return ldapTemplateSlaveList;
    }

    /**
     * 构建ldapTemplate实例
     *
     * @return
     */
    public void buildLdapTemplate() {
        if (ldapTemplate == null)
            ldapTemplate = buildLdapTemplate(ldapUrl);
        if (StringUtils.isEmpty(ldapSlaveUrl)) return;
        if (ldapTemplateSlaveList != null && ldapTemplateSlaveList.size() != 0) return;
        try {
            ldapTemplateSlaveList = new ArrayList<>();
            for (String url : ldapSlaveUrl.split(","))
                ldapTemplateSlaveList.add(buildLdapTemplate(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LdapTemplate buildLdapTemplate(String url) {
        //if (ldapTemplate != null) return ldapTemplate;
        LdapContextSource contextSource = buildLdapContextSource(new LdapDO(url, manageDn, ldapManagePasswd));
        TransactionAwareContextSourceProxy sourceProxy = buildTransactionAwareContextSourceProxy(contextSource);
        LdapTemplate ldapTemplate = new LdapTemplate(sourceProxy);
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
