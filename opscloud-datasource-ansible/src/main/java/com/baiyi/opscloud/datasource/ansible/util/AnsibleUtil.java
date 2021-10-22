package com.baiyi.opscloud.datasource.ansible.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.datasource.ansible.param.AnsibleParam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author baiyi
 * @Date 2021/8/31 2:01 下午
 * @Version 1.0
 */
public class AnsibleUtil {

    /**
     * 转换 Ansible Playbook 外部变量
     *
     * @param vars
     * @return
     */
    public static AnsibleParam.ExtraVars toVars(String vars) {
        if (StringUtils.isEmpty(vars))
            return AnsibleParam.ExtraVars.EMPTY;
        try {
            Yaml yaml = new Yaml();
            Object result = yaml.load(vars);
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(JSON.toJSONString(result), AnsibleParam.ExtraVars.class);
        } catch (Exception e) {
            return AnsibleParam.ExtraVars.EMPTY;
        }
    }

//    public static PlaybookTags toTags(String ymlTags) {
//        Yaml yaml = new Yaml();
//        Object result = yaml.load(tagsYAML);
//        try {
//            Gson gson = new GsonBuilder().create();
//            PlaybookTags tags = gson.fromJson(JSON.toJSONString(result), PlaybookTags.class);
//            return tags;
//        } catch (Exception e) {
//            return new PlaybookTags();
//        }
//    }

    /**
     * @param ansibleResult
     * @return '10.200.1.40 | CHANGED => '
     */
    public static String getResultHead(String ansibleResult) {
        /**    10.200.1.40 | CHANGED => {    **/
        try {
            Pattern pattern = Pattern.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\ \\|\\ \\w+\\ =>\\ ");
            Matcher matcher = pattern.matcher(ansibleResult);
            if (matcher.find())
                return matcher.group(0);
        } catch (Exception e) {
        }
        return "";
    }
}
