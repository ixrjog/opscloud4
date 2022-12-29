package com.baiyi.opscloud.common.template;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

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
            Representer representer = new Representer();
            representer.getPropertyUtils().setSkipMissingProperties(true);
            Yaml yaml = new Yaml(new Constructor(YamlVars.Vars.class),representer);
            return yaml.loadAs(vars, YamlVars.Vars.class);
        } catch (Exception e) {
            return YamlVars.Vars.EMPTY;
        }
    }
}
