package com.baiyi.opscloud.ansible.builder;

import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import org.apache.commons.exec.CommandLine;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/4/6 5:27 下午
 * @Version 1.0
 */
public class AnsibleArgsBuilder {

    public static CommandLine build(AnsibleConfig config, AnsibleArgsBO args) {
        CommandLine commandLine = new CommandLine("bin");

        if(!StringUtils.isEmpty(args.getInventory())){
            commandLine.addArgument("-i");
            commandLine.addArgument(args.getInventory());
        }

        if(!StringUtils.isEmpty(args.getModuleName())){
            commandLine.addArgument("-m");
            commandLine.addArgument(args.getModuleName());
        }

        if(!StringUtils.isEmpty(args.getModuleArguments())){
            commandLine.addArgument("-a");
            commandLine.addArgument(args.getModuleArguments());
        }

        if(!StringUtils.isEmpty(args.getBecomeUser()) && !args.getBecomeUser().equalsIgnoreCase("root")){
            commandLine.addArgument("--become-user");
            commandLine.addArgument(args.getBecomeUser());
        }

        if(args.getForks() != null && args.getForks() != 5){
            commandLine.addArgument("-f");
            commandLine.addArgument(args.getForks().toString());
        }
        return commandLine;
    }
}
