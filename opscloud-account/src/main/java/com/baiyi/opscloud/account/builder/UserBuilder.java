package com.baiyi.opscloud.account.builder;

import com.baiyi.opscloud.account.bo.UserBO;
import com.baiyi.opscloud.account.config.AuthConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.ldap.entry.Person;
import com.baiyi.opscloud.zabbix.entry.ZabbixUser;
import org.gitlab.api.models.GitlabUser;

/**
 * @Author baiyi
 * @Date 2020/1/15 9:22 上午
 * @Version 1.0
 */
public class UserBuilder {

    public static OcUser build(Person person) {
        UserBO bo = UserBO.builder()
                .username(person.getUsername())
                .displayName(person.getDisplayName())
                .email(person.getEmail())
                .phone(person.getMobile())
                .source("ldap")
                .build();
        return convert(bo);
    }

    public static OcUser build(ZabbixUser zabbixUser) {
        UserBO bo = UserBO.builder()
                .username(zabbixUser.getAlias())
                .displayName(zabbixUser.getName())
                .source("zabbix")
                .build();
        return convert(bo);
    }

    public static OcUser build(AuthConfig.Admin admin) {
        UserBO bo = UserBO.builder()
                .username(admin.getUsername())
                .displayName(admin.getUsername())
                .source("local")
                .build();
        return convert(bo);
    }

    public static OcUser build(GitlabUser gitlabUser) {
        UserBO bo = UserBO.builder()
                .username(gitlabUser.getUsername())
                .displayName(gitlabUser.getName())
                .email(gitlabUser.getEmail())
                .isActive("active".equals(gitlabUser.getState()))
                .source("gitlab")
                .build();
        return convert(bo);
    }

    private static OcUser convert(UserBO bo) {
        return BeanCopierUtils.copyProperties(bo, OcUser.class);
    }

}
