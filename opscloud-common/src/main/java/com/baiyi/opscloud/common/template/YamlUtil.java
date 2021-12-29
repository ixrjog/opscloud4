package com.baiyi.opscloud.common.template;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * @Author baiyi
 * @Date 2021/12/6 7:31 PM
 * @Version 1.0
 */
public class YamlUtil {

    /**
     * 转换 Ansible Playbook 外部变量
     *
     * @param vars
     * @return
     */
    public static YamlVars.Vars toVars(String vars) {
        if (StringUtils.isEmpty(vars))
            return YamlVars.Vars.EMPTY;
        try {
            Yaml yaml = new Yaml();
            Object result = yaml.load(vars);
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(JSONUtil.writeValueAsString(result), YamlVars.Vars.class);
        } catch (Exception e) {
            return YamlVars.Vars.EMPTY;
        }
    }
}
