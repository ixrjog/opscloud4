package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/3/3 2:18 下午
 * @Version 1.0
 */
public class Global {

    public static final String CREATED_BY = "Created by opscloud";

    public static final String SERVER_ATTRIBUTE_GLOBAL_ENABLE_PUBLIC_IP_MGMT = "global_enable_public_ip_mgmt";

    public static final String SERVER_ATTRIBUTE_GLOBAL_SSH_PORT = "ssh_port";

    public static final String SERVER_ATTRIBUTE_ANSIBLE_SUBGROUP = "ansible_subgroup";

    public static final String SERVER_ATTRIBUTE_ZABBIX_BIDIRECTIONAL_SYNC = "zabbix_bidirectional_sync";
    public static final String SERVER_ATTRIBUTE_ZABBIX_PROXY = "zabbix_proxy";
    public static final String SERVER_ATTRIBUTE_ZABBIX_TEMPLATES = "zabbix_templates";
    public static final String SERVER_ATTRIBUTE_ZABBIX_HOST_MACROS = "zabbix_host_macros";
    public static final String SERVER_ATTRIBUTE_ZABBIX_SYSDISK_VOLUME_NAME = "zabbix_sysdisk_volume_name";
    public static final String SERVER_ATTRIBUTE_ZABBIX_DATADISK_VOLUME_NAME = "zabbix_datadisk_volume_name";


    public static final String ASYNC_POOL_TASK_EXECUTOR = "asyncPoolTaskExecutor";

    public static final String ASYNC_POOL_TASK_COMMON = "asyncPoolTaskCommon";

    public static final String BASE_ROLE_NAME = "base";

    public static final String DEV_ROLE_NAME = "dev";

    // 高权限账户
    // public static final String HIGH_AUTHORITY_ACCOUNT = "admin";

    public static final String ENV_PROD = "prod";

    public interface envNames{
        String PROD = "prod";
        String DEFAULT = "default";
    }

    public static final String JUMPSERVER_ADMIN_TOKEN = "jumpserverAdminToken";


}
