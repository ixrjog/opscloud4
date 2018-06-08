package com.sdg.cmdb.handler;

import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.ldap.LdapDO;
import com.sdg.cmdb.plugin.ldap.LDAPFactory;
import com.sdg.cmdb.service.ConfigCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Created by zxxiao on 2017/6/26.
 */
@Service
public class LdapHandler implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(LdapHandler.class);

    //private HashMap<String, String> configMap;

    @Resource
    private LDAPFactory ldapFactory;

    @Value("#{cmdb['ldapUrl']}")
    private String ldapUrl;

    @Value("#{cmdb['ldapUserDn']}")
    private String ldapUserDn;

    @Value("#{cmdb['ldapPwd']}")
    private String ldapPwd;



//    @Resource
//    private ConfigCenterService configCenterService;

    @Override
    public void afterPropertiesSet() throws Exception {
        initLdapInstance();
    }

//    private HashMap<String, String> acqConifMap() {
//        if (configMap != null) return configMap;
//        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.LDAP.getItemKey());
//    }

    /**
     * 初始化ldap实例
     */
    private void initLdapInstance() {
//        HashMap<String, String> configMap = acqConifMap();
//        for(LdapDO.LdapTypeEnum typeEnum : LdapDO.LdapTypeEnum.values()) {
//            ldapFactory.buildLdapTemplate(new LdapDO(configMap, typeEnum.getDesc()));
//        }
        ldapFactory.buildLdapTemplate( new LdapDO(ldapUrl,ldapUserDn,ldapPwd));

    }
}
