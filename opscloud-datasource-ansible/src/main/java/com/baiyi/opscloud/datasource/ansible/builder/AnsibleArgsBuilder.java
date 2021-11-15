package com.baiyi.opscloud.datasource.ansible.builder;


import com.baiyi.opscloud.datasource.ansible.args.CommandArgs;
import com.baiyi.opscloud.common.datasource.AnsibleConfig;
import com.google.common.base.Joiner;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;


/**
 * @Author baiyi
 * @Date 2020/4/16 2:04 下午
 * @Version 1.0
 */
public class AnsibleArgsBuilder {


    public final static String ANSIBLE_HOSTS = "ansible_hosts";

    public final static String ANSIBLE_PLAYBOOK = "playbook";

    /**
     * ansible 通用参数构建
     *
     * @param ansible
     * @param args
     * @return
     */
    public static CommandLine build(AnsibleConfig.Ansible ansible, CommandArgs args) {
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
    public static CommandLine buildPlaybook(AnsibleConfig.Ansible ansible, CommandArgs args) {
        CommandLine commandLine = new CommandLine(ansible.getPlaybook());
        return getCommandLine(commandLine, ansible, args);
    }

    public static CommandLine getCommandLine(CommandLine commandLine, AnsibleConfig.Ansible ansible, CommandArgs args) {
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
            commandLine.addArgument(ansible.getPrivateKey());
        }

        // 指定主机文件，如果不指定则用默认主机文件
        commandLine.addArgument("-i");
        if (!StringUtils.isEmpty(args.getInventory())) {
            commandLine.addArgument(args.getInventory());
        } else {
            String hosts = Joiner.on("/").join(ansible.getData(), "inventory", ANSIBLE_HOSTS);
            commandLine.addArgument(hosts);
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
