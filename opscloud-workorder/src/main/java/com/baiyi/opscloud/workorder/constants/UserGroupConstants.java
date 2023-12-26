package com.baiyi.opscloud.workorder.constants;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2022/1/7 11:00 AM
 * @Version 1.0
 */
@Getter
public enum UserGroupConstants {

    /**
     * LDAP用户组
     */
    VPN_USERS("vpn-users"),
    NEXUS_USERS("nexus-users"),
    NEXUS_DEVELOPER("nexus-developer"),
    CONFLUENCE_USERS("confluence-users"),
    GRAFANA_ADMIN("grafana-admin"),
    GRAFANA_EDITOR_USERS("grafana-editor-users"),
    GRAFANA_USERS("grafana-users"),
    APOLLO_USERS("apollo-users");

    UserGroupConstants(String role) {
        this.role = role;
    }

    private final String role;

}