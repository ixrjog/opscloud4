package com.baiyi.opscloud.common.util;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/3 5:32 下午
 * @Version 1.0
 */
public class TemplateUtil {

    public static String render(String templateString, Map<String, String> dict) {
        try {
            StringSubstitutor sub = new StringSubstitutor(dict);
            return sub.replace(templateString);
        } catch (Exception e) {
            return templateString;
        }
    }

}