package com.baiyi.opscloud.ldap.config;

import com.google.common.base.Joiner;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/1/3 3:34 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "ldap", ignoreInvalidFields = true)
public class LdapConfig {

    @Value("${spring.ldap.base}")
    private String base;

    private Map<String, String> custom;

    /**
     * custom:  {userId: cn,
     * userBaseDN: ou=users,
     * userObjectClass: inetorgperson,
     * groupId: cn,
     * groupBaseDN: ou=groups,
     * groupObjectClass: groupOfUniqueNames,
     * groupMember: uniqueMember}
     */

    public static final String USER_ID = "userId";
    public static final String USER_BASE_DN = "userBaseDN";
    public static final String USER_OBJECT_CLASS = "userObjectClass";
    public static final String GROUP_OBJECT_CLASS = "groupObjectClass";
    public static final String GROUP_MEMBER = "groupMember";
    public static final String GROUP_BASE_DN = "groupBaseDN";
    public static final String GROUP_ID = "groupId";


    public String getCustomByKey(String key) {
        return custom.get(key);
    }

    public String buildUserDN(String username) {
        String rdn = Joiner.on("=").join(getCustomByKey(USER_ID), username);
        return Joiner.on(",").skipNulls().join(rdn, getCustomByKey(USER_BASE_DN));
    }

    public String buildUserFullDN(String username) {
        if(StringUtils.isEmpty(base)){
            return buildUserDN(username);
        }else{
            return Joiner.on(",").join(buildUserDN(username), base);
        }
    }

    public String buildGroupDN(String groupName){
        String rdn = Joiner.on("=").join(getCustomByKey(GROUP_ID), groupName);
        return Joiner.on(",").skipNulls().join(rdn, getCustomByKey(GROUP_BASE_DN));
    }
}
