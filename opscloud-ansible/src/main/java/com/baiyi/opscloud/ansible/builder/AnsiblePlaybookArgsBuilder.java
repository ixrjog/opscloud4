package com.baiyi.opscloud.ansible.builder;

import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.apache.commons.exec.CommandLine;
import org.springframework.util.StringUtils;

import java.util.Map;

import static com.baiyi.opscloud.ansible.config.AnsibleConfig.ANSIBLE_HOSTS;

/**
 * @Author baiyi
 * @Date 2020/4/12 1:14 下午
 * @Version 1.0
 */
public class AnsiblePlaybookArgsBuilder {

    /**
     * @param config
     * @param args
     * @return
     */
    public static CommandLine build(AnsibleConfig config, AnsiblePlaybookArgsBO args) {
        CommandLine commandLine = new CommandLine(config.getPlaybookBin());

        if (args.isVersion()) {
            commandLine.addArgument("--version");
            return commandLine;
        }

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

        if (!StringUtils.isEmpty(args.getBecomeUser()) && !args.getBecomeUser().equalsIgnoreCase("root")) {
            commandLine.addArgument("--become-user");
            commandLine.addArgument(args.getBecomeUser());
        }

        if (args.getForks() != null && args.getForks() != 5) {
            commandLine.addArgument("-f");
            commandLine.addArgument(args.getForks().toString());
        }

        // 外部变量
        Map<String, String> extraVarsMap = Maps.newHashMap();
        if (args.getExtraVars() != null)
            extraVarsMap = args.getExtraVars();

        extraVarsMap.put("hosts", args.getHosts());
        commandLine.addArgument("-e");
        commandLine.addArgument(convertExtraVars(extraVarsMap),false);

        // 标签执行 -t task1,task2
        if (args.getTags() != null && !args.getTags().isEmpty()) {
            commandLine.addArgument("-t");
            commandLine.addArgument(Joiner.on(",").join(args.getTags()));
        }

        // playbook脚本
        commandLine.addArgument(args.getPlaybook());

        return commandLine;
    }

    private static String convertExtraVars(Map<String, String> extraVarsMap) {
        String extraVars = null;
        for (String key : extraVarsMap.keySet())
            extraVars = Joiner.on(" ").skipNulls().join(extraVars, Joiner.on("=").join(key, extraVarsMap.get(key)));
        return extraVars;
    }
}
