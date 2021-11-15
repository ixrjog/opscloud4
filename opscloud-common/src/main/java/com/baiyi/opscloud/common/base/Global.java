package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/3/3 2:18 下午
 * @Version 1.0
 */
public class Global {

    public static final String SUPER_ADMIN = "super_admin";

    public interface TaskPools {
        String EXECUTOR = "asyncExecutorTaskPool";
        String DEFAULT = "asyncDefaultTaskPool";
    }


    public static final String CREATED_BY = "Created by opscloud";

    // extend
    public static final int EXTEND = 1;
    public static final int NOT_EXTEND = 0;

    public static final String SERVER_ATTRIBUTE_GLOBAL_ENABLE_PUBLIC_IP_MGMT = "global_enable_public_ip_mgmt";

    public static final String SERVER_ATTRIBUTE_GLOBAL_SSH_PORT = "ssh_port";
    public static final String SERVER_ATTRIBUTE_GLOBAL_ADMIN_ACCOUNT = "admin_account";

    public static final String SERVER_ATTRIBUTE_ANSIBLE_SUBGROUP = "ansible_subgroup";

    public static final String BASE_ROLE_NAME = "base";

    // 高权限账户
    // public static final String HIGH_AUTHORITY_ACCOUNT = "admin";

    public static final String ENV_PROD = "prod";

    public static final String ENV_GRAY = "gray";


    // Jenkins Build
    public static final String SERVER_GROUP = "serverGroup";

    public static final String HOST_PATTERN = "hostPattern";

    public static final String BRANCH = "branch";

}
