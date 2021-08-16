package com.baiyi.opscloud.common.datasource;


import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.config.DsLdapConfig;
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

    private DsLdapConfig.Ldap ldap;

}
