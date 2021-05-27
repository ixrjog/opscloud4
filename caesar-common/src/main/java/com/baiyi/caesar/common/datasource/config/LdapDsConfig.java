package com.baiyi.caesar.common.datasource.config;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/5/15 6:46 下午
 * @Version 1.0
 */
public class LdapDsConfig {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Ldap {
        private String url;
        private String base;
        private LdapManage manager; // 管理员账户
        private LdapUser user;
        private LdapGroup group;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LdapManage {
        private String dn;  // 管理员账户
        private String password; // 管理员账户密码
    }

    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class LdapUser extends BaseLdapObject {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LdapGroup extends BaseLdapObject {
        private String memberAttribute;
        private String memberFormat;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class BaseLdapObject {
        private String id;
        private String dn;
        private String objectClass;
    }

}
