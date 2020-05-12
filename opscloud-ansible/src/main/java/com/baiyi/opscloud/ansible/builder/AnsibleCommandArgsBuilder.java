package com.baiyi.opscloud.ansible.builder;

import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import org.apache.commons.exec.CommandLine;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/4/6 5:27 下午
 * @Version 1.0
 */
public class AnsibleCommandArgsBuilder {

    /**
     * @param config
     * @param args
     * @return
     */
    public static CommandLine build(AnsibleConfig config, AnsibleArgsBO args) {
        CommandLine commandLine = AnsibleArgsBuilder.build(config,args);

        if(args.isVersion())
            return commandLine;

        commandLine.addArgument("-m");
        if (!StringUtils.isEmpty(args.getModuleName())) {
            commandLine.addArgument(args.getModuleName());
        }else{
            commandLine.addArgument("shell");
        }

        if (!StringUtils.isEmpty(args.getModuleArguments())) {
            commandLine.addArgument("-a");
            commandLine.addArgument(args.getModuleArguments());
        }

        return commandLine;
    }
}
