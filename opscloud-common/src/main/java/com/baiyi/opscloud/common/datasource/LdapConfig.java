package com.baiyi.opscloud.common.datasource;


import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.google.common.base.Joiner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/5/17 1:34 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LdapConfig extends BaseDsConfig {

    private Ldap ldap;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Ldap {

        private static final String RDN = "{}={}";

        private String url;
        private String base;
        private LdapManage manager; // 管理员账户
        private LdapUser user;
        private LdapGroup group;

        public String buildUserDn(String username) {
            return Joiner.on(",").skipNulls().join(StringFormatter.arrayFormat(RDN, user.getId(), username), user.getDn());
        }

        public String buildGroupDn(String groupName) {
            return Joiner.on(",").skipNulls().join(StringFormatter.arrayFormat(RDN, group.getId(), groupName), group.getDn());
        }

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class LdapManage {
        private String dn;  // 管理员账户
        private String password; // 管理员账户密码
    }

    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class LdapUser extends BaseLdapObject {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class LdapGroup extends BaseLdapObject {
        private String memberAttribute;
        private String memberFormat;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class BaseLdapObject {
        private String id;
        private String dn;
        private String objectClass;
    }

}