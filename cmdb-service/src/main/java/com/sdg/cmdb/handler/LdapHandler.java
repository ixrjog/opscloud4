package com.sdg.cmdb.handler;


import com.sdg.cmdb.plugin.ldap.LDAPFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;


@Service
public class LdapHandler implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(LdapHandler.class);

    @Resource
    private LDAPFactory ldapFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        initLdapInstance();
    }

    /**
     * 初始化ldap实例
     */
    private void initLdapInstance() {
        ldapFactory.buildLdapTemplate();
    }
}
