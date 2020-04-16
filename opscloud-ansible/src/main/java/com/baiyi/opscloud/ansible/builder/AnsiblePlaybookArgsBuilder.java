package com.baiyi.opscloud.ansible.builder;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.ansible.config.AnsibleConfig;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.commons.exec.CommandLine;

import java.util.Map;

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

        CommandLine commandLine = AnsibleArgsBuilder.buildPlaybook(config, new Gson().fromJson(JSON.toJSONString(args), AnsibleArgsBO.class));

        if (args.isVersion())
            return commandLine;

        // 外部变量
        Map<String, String> extraVarsMap = Maps.newHashMap();
        if (args.getExtraVars() != null)
            extraVarsMap = args.getExtraVars();

        extraVarsMap.put("hosts", args.getHosts());
        commandLine.addArgument("-e");
        commandLine.addArgument(convertExtraVars(extraVarsMap), false);

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
