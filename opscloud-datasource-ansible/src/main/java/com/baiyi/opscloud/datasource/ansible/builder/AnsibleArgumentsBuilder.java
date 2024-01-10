package com.baiyi.opscloud.datasource.ansible.builder;


import com.baiyi.opscloud.common.datasource.AnsibleConfig;
import com.baiyi.opscloud.datasource.ansible.builder.args.AnsibleCommandArgs;
import com.google.common.base.Joiner;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;


/**
 * @Author baiyi
 * @Date 2020/4/16 2:04 下午
 * @Version 1.0
 */
public class AnsibleArgumentsBuilder {


    public final static String ANSIBLE_HOSTS = "ansible_hosts";

    public final static String ANSIBLE_PLAYBOOK = "playbook";

    /**
     * ansible 通用参数构建
     *
     * @param ansible
     * @param args
     * @return
     */
    public static CommandLine build(AnsibleConfig.Ansible ansible, AnsibleCommandArgs args) {
        CommandLine commandLine = new CommandLine(ansible.getAnsible());
        return getCommandLine(commandLine, ansible, args);
    }

    /**
     * ansible-playbook 通用参数构建
     *
     * @param ansible
     * @param args
     * @return
     */
    public static CommandLine buildPlaybook(AnsibleConfig.Ansible ansible, AnsibleCommandArgs args) {
        CommandLine commandLine = new CommandLine(ansible.getPlaybook());
        return getCommandLine(commandLine, ansible, args);
    }

    public static CommandLine getCommandLine(CommandLine commandLine, AnsibleConfig.Ansible ansible, AnsibleCommandArgs args) {
        if (args.isVersion()) {
            commandLine.addArgument("--version");
            return commandLine;
        }
        // 目标主机或分组
        commandLine.addArgument(args.getPattern());

        commandLine.addArgument("--key-file");
        commandLine.addArgument(StringUtils.isEmpty(args.getKeyFile()) ? ansible.getPrivateKey() : args.getKeyFile());

        // 指定主机文件，如果不指定则用默认主机文件
        commandLine.addArgument("-i");
        commandLine.addArgument(StringUtils.isEmpty(args.getInventory()) ? Joiner.on("/").join(ansible.getData(), "inventory", ANSIBLE_HOSTS)
                : args.getInventory());

        // become
        commandLine.addArgument("--become");
        commandLine.addArgument("--become-method=sudo");
        commandLine.addArgument("--become-user");
        commandLine.addArgument(StringUtils.isEmpty(args.getBecomeUser()) ? "root" : args.getBecomeUser());

        // forks
        if (args.getForks() != null && args.getForks() != 5) {
            commandLine.addArgument("-f");
            commandLine.addArgument(args.getForks().toString());
        }

        return commandLine;
    }

}