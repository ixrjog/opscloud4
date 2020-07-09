package com.baiyi.opscloud.common.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.domain.vo.kubernetes.templateVariable.TemplateVariable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * @Author baiyi
 * @Date 2020/7/3 4:29 下午
 * @Version 1.0
 */
public class KubernetesUtils {

    // TemplateVariable
    public static TemplateVariable buildVariable(String templateVariableYAML) {
        if (StringUtils.isEmpty(templateVariableYAML))
            return null;
        try {
            Yaml yaml = new Yaml();
            Object result = yaml.load(templateVariableYAML);
            Gson gson = new GsonBuilder().create();
            TemplateVariable templateVariable = gson.fromJson(JSON.toJSONString(result), TemplateVariable.class);
            return templateVariable;
        } catch (Exception e) {
            return null;
        }
    }

}
