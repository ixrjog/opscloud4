package com.baiyi.opscloud.ansible.builder;

import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.google.common.base.Joiner;
import org.apache.commons.exec.CommandLine;
import org.springframework.util.StringUtils;

import static com.baiyi.opscloud.ansible.config.AnsibleConfig.ANSIBLE_HOSTS;

/**
 * @Author baiyi
 * @Date 2020/4/16 2:04 下午
 * @Version 1.0
 */
public class AnsibleArgsBuilder {

    /**
     * ansible 通用参数构建
     *
     * @param config
     * @param args
     * @return
     */
    public static CommandLine build(AnsibleConfig config, AnsibleArgsBO args) {
        CommandLine commandLine = new CommandLine(config.getBin());
        return getCommandLine(commandLine, config, args);
    }

    /**
     * ansible-playbook 通用参数构建
     *
     * @param config
     * @param args
     * @return
     */
    public static CommandLine buildPlaybook(AnsibleConfig config, AnsibleArgsBO args) {
        CommandLine commandLine = new CommandLine(config.getPlaybookBin());
        return getCommandLine(commandLine, config, args);
    }

    public static CommandLine getCommandLine(CommandLine commandLine, AnsibleConfig config, AnsibleArgsBO args) {
        if (args.isVersion()) {
            commandLine.addArgument("--version");
            return commandLine;
        }
        // 目标主机或分组
        commandLine.addArgument(args.getPattern());

        commandLine.addArgument("--key-file");
        if (!StringUtils.isEmpty(args.getKeyFile())) {
            commandLine.addArgument(args.getKeyFile());
        } else {
            commandLine.addArgument(config.acqPrivateKey());
        }

        // 指定主机文件，如果不指定则用默认主机文件
        commandLine.addArgument("-i");
        if (!StringUtils.isEmpty(args.getInventory())) {
            commandLine.addArgument(args.getInventory());
        } else {
            commandLine.addArgument(Joiner.on("/").join(config.acqInventoryPath(), ANSIBLE_HOSTS));
        }

        commandLine.addArgument("--become");
        commandLine.addArgument("--become-method=sudo");
        commandLine.addArgument("--become-user");
        if (StringUtils.isEmpty(args.getBecomeUser())) {
            commandLine.addArgument("root");
        } else {
            commandLine.addArgument(args.getBecomeUser());
        }

        if (args.getForks() != null && args.getForks() != 5) {
            commandLine.addArgument("-f");
            commandLine.addArgument(args.getForks().toString());
        }

        return commandLine;
    }


}
