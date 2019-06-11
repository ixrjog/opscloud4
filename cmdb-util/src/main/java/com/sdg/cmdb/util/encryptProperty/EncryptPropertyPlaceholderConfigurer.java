package com.sdg.cmdb.util.encryptProperty;

import com.sdg.cmdb.util.KeyPairGenUtil;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 *  配置文件解密
 */
public class EncryptPropertyPlaceholderConfigurer extends
        PropertyPlaceholderConfigurer {

    /**
     * 需要解密的配置项
     */
    private String[] encryptPropNames = {"jdbc_url", "jdbc_user", "jdbc_password",
            "jumpserver_jdbc_url", "jumpserver_jdbc_user", "jumpserver_jdbc_password", "jumpserver.user", "jumpserver.passwd",
            "ldap.url", "ldap.manager.passwd",
            "redis.pwd",
            "email.passwd",
            "gitlab.token",
            "jenkins.user","jenkins.token",
            "zabbix.user","zabbix.passwd",
            "aliyun.access.key","aliyun.access.secret",
            "gateway.admin.test.host","gateway.admin.test.ak",
            "gateway.admin.pre.host","gateway.admin.pre.ak",
            "gateway.admin.prod.host","gateway.admin.prod.ak"
    };

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            String decryptValue = KeyPairGenUtil.decrypt(propertyValue);
            return decryptValue;
        } else {
            return propertyValue;
        }
    }

    private boolean isEncryptProp(String propertyName) {
        for (String encryptpropertyName : encryptPropNames) {
            if (encryptpropertyName.equals(propertyName))
                return true;
        }
        return false;
    }
}
