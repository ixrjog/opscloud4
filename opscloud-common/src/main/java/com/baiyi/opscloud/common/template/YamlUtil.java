package com.baiyi.opscloud.common.template;

import org.apache.commons.lang3.StringUtils;

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
        if (StringUtils.isEmpty(vars)) {
            return YamlVars.Vars.EMPTY;
        }
        try {
            return com.baiyi.opscloud.common.util.YamlUtil.loadAs(vars, YamlVars.Vars.class);
        } catch (Exception e) {
            return YamlVars.Vars.EMPTY;
        }
    }

}
