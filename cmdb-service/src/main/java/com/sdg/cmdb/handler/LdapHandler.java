package com.sdg.cmdb.handler;


import com.sdg.cmdb.plugin.ldap.LDAPFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Slf4j
@Service
public class LdapHandler implements InitializingBean {

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
