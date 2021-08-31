package com.baiyi.opscloud.ansible.builder;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.ansible.args.CommandArgs;
import com.baiyi.opscloud.ansible.args.PlaybookArgs;
import com.baiyi.opscloud.common.datasource.config.DsAnsibleConfig;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/4/12 1:14 下午
 * @Version 1.0
 */
public class AnsiblePlaybookArgsBuilder {

    /**
     * @param ansible
     * @param args
     * @return
     */
    public static CommandLine build(DsAnsibleConfig.Ansible ansible, PlaybookArgs args) {
        CommandLine commandLine = AnsibleArgsBuilder.buildPlaybook(ansible, new Gson().fromJson(JSON.toJSONString(args), CommandArgs.class));
        if (args.isVersion())
            return commandLine;
        // 外部变量
        Map<String, String> extraVars = args.getExtraVars();
        if (!StringUtils.isEmpty(args.getHosts()))
            extraVars.put("hosts", args.getHosts());

        commandLine.addArgument("-e");
        commandLine.addArgument(toExtraVars(extraVars), false);

        // 标签执行 -t task1,task2
        if (args.getTags() != null && !args.getTags().isEmpty()) {
            commandLine.addArgument("-t");
            commandLine.addArgument(Joiner.on(",").join(args.getTags()));
        }

        // playbook脚本
        commandLine.addArgument(args.getPlaybook());
        return commandLine;
    }

    private static String toExtraVars(Map<String, String> extraVars) {
        return Joiner.on(" ").skipNulls().join(
                extraVars.keySet().stream().map(k ->
                        Joiner.on("=").join(k, extraVars.get(k))
                ).collect(Collectors.toList())
        );
    }
}
