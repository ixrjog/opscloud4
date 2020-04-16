package com.baiyi.opscloud.ansible.builder;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/6 5:16 下午
 * @Version 1.0
 */
@Data
@Builder
public class AnsibleArgsBO {


    /**
     * host pattern
     */
    private String pattern;

    /**
     *  --private-key PRIVATE_KEY_FILE, --key-file PRIVATE_KEY_FILE
     *                         use this file to authenticate the connection
     */
    private String keyFile;

    /**
     *  -i INVENTORY, --inventory INVENTORY, --inventory-file INVENTORY
     *                         specify inventory host path or comma separated host
     *                         list. --inventory-file is deprecated
     */
    private String inventory;

    /**
     *   -m MODULE_NAME, --module-name MODULE_NAME
     *                         module name to execute (default=command)
     */
    private String moduleName;

    /**
     *   -a MODULE_ARGS, --args MODULE_ARGS
     *                         module arguments
     */
    private String moduleArguments;

    /**
     * script 参数
     */
    private String scriptParam;
    /**
     *   -f FORKS, --forks FORKS
     *                         specify number of parallel processes to use
     *                         (default=5)
     */
    @Builder.Default
    private Integer forks = 1;

    /**
     *   --become-user BECOME_USER
     *                         run operations as this user (default=root)
     */
    @Builder.Default
    private String becomeUser = "root";

    @Builder.Default
    private boolean version = false;

    /**
     *   -v, --verbose         verbose mode (-vvv for more, -vvvv to enable
     *                         connection debugging)
     */
    private String verbose;

}
