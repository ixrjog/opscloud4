package com.baiyi.opscloud.datasource.ansible.builder.args;

import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/12/28 3:48 PM
 * @Version 1.0
 */
public class AnsibleArgs {
    /**
     * @Author baiyi
     * @Date 2021/8/16 2:18 下午
     * @Version 1.0
     */
    @Data
    @Builder
    public static class Playbook implements IArgs {

        /**
         * Playbook(s)
         */
        private String playbook;

        /**
         * hosts 主机ip
         */
        private String hosts;

        /**
         * --private-key PRIVATE_KEY_FILE, --key-file PRIVATE_KEY_FILE
         * use this file to authenticate the connection
         */
        private String keyFile;

        /**
         * -i INVENTORY, --inventory INVENTORY, --inventory-file INVENTORY
         * specify inventory host path or comma separated host
         * list. --inventory-file is deprecated
         */
        private String inventory;

        /**
         * -t TAGS, --tags TAGS  only run plays and tasks tagged with these values
         */
        private Set<String> tags;


        @Builder.Default
        private Integer forks = 1;

        /**
         * --become-user BECOME_USER
         * run operations as this user (default=root)
         */
        @Builder.Default
        private String becomeUser = "root";

        /**
         * -e EXTRA_VARS, --extra-vars EXTRA_VARS
         * set additional variables as key=value or YAML/JSON, if filename prepend with @
         */
        @Builder.Default
        private Map<String, String> extraVars = Maps.newHashMap();

        /**
         * -v, --verbose         verbose mode (-vvv for more, -vvvv to enable
         * connection debugging)
         */
        private String verbose;

        @Builder.Default
        private boolean version = false;
    }

    /**
     * @Author baiyi
     * @Date 2021/6/22 5:27 下午
     * @Version 1.0
     */
    @Data
    @Builder
    public static class Command implements IArgs {

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

    /**
     * @Author baiyi
     * @Date 2021/8/31 10:50 上午
     * @Version 1.0
     */
    public interface IArgs {

        String getInventory();
        String getKeyFile();
        Integer getForks();
        String getBecomeUser();
        boolean isVersion();

    }

}
