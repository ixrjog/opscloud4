package com.baiyi.caesar.datasource.ansible.command.base;

import com.baiyi.caesar.common.datasource.AnsibleDsInstanceConfig;
import com.baiyi.caesar.datasource.ansible.args.CommandArgs;
import org.apache.commons.exec.CommandLine;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2021/6/22 5:28 下午
 * @Version 1.0
 */
public class CommandLineBuilder {

    public final static String ANSIBLE_HOSTS = "ansible_hosts";

    public final static String ANSIBLE_PLAYBOOK = "playbook";

    /**
     * ansible 通用参数构建
     *
     * @param args
     * @return
     */
    public static CommandLine build(AnsibleDsInstanceConfig config, CommandArgs args) {
        CommandLine commandLine = new CommandLine(config.getAnsible().getBin());
        return build(config, commandLine, args);
    }


    public static CommandLine build(AnsibleDsInstanceConfig config, CommandLine commandLine, CommandArgs args) {
        if (args.isVersion()) {
            commandLine.addArgument("--version");
            return commandLine;
        }
        // 目标主机或分组
        commandLine.addArgument(args.getPattern());

        commandLine.addArgument("--key-file")
                .addArgument(StringUtils.isEmpty(args.getKeyFile()) ? config.getAnsible().getPrivateKey() : args.getKeyFile())
                // 指定主机文件，如果不指定则用默认主机文件
                .addArgument("-i")
                .addArgument(args.getInventory())
                .addArgument("--become")
                .addArgument("--become-method=sudo")
                .addArgument("--become-user")
                .addArgument(StringUtils.isEmpty(args.getBecomeUser()) ? "root" : args.getBecomeUser());

        if (args.getForks() != null && args.getForks() != 5) {
            commandLine.addArgument("-f")
                    .addArgument(args.getForks().toString());
        }

        return commandLine;
    }
}
