package com.baiyi.opscloud.ansible.builder;

import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.google.common.base.Joiner;
import org.apache.commons.exec.CommandLine;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/4/16 1:56 下午
 * @Version 1.0
 */
public class AnsibleScriptArgsBuilder {

    /**
     * @param config
     * @param args
     * @return
     */
    public static CommandLine build(AnsibleConfig config, AnsibleArgsBO args) {
        CommandLine commandLine = AnsibleArgsBuilder.build(config, args);

        if (args.isVersion())
            return commandLine;

        commandLine.addArgument("-m");
        if (!StringUtils.isEmpty(args.getModuleName())) {
            commandLine.addArgument(args.getModuleName());
        } else {
            commandLine.addArgument("script");
        }

        if (!StringUtils.isEmpty(args.getModuleArguments())) {
            commandLine.addArgument("-a");
            if (StringUtils.isEmpty(args.getScriptParam())) {
                commandLine.addArgument(args.getModuleArguments());
            } else {
                // 支持带参数的脚本
                commandLine.addArgument(Joiner.on(" ").join(args.getModuleArguments(), args.getScriptParam()), false);
            }
        }

        return commandLine;
    }
}
