package com.baiyi.caesar.common.datasource;


import com.baiyi.caesar.common.datasource.config.LdapDsConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/5/17 1:34 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LdapDsInstanceConfig extends BaseDsInstanceConfig {

    private LdapDsConfig.Ldap ldap;
}
