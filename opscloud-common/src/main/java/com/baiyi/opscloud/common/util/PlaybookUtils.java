package com.baiyi.opscloud.common.util;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.domain.vo.ansible.playbook.PlaybookTags;
import com.baiyi.opscloud.domain.vo.ansible.playbook.PlaybookVars;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * @Author baiyi
 * @Date 2020/4/14 6:55 下午
 * @Version 1.0
 */
public class PlaybookUtils {

    public static PlaybookVars buildVars(String varsYAML) {
        if(StringUtils.isEmpty(varsYAML))
            return new PlaybookVars();
        try {
            Yaml yaml = new Yaml();
            Object result = yaml.load(varsYAML);
            Gson gson = new GsonBuilder().create();
            PlaybookVars vars = gson.fromJson(JSON.toJSONString(result), PlaybookVars.class);
            return vars;
        } catch (Exception e) {
            return new PlaybookVars();
        }
    }

    public static PlaybookTags buildTags(String tagsYAML) {
        Yaml yaml = new Yaml();
        Object result = yaml.load(tagsYAML);
        try {
            Gson gson = new GsonBuilder().create();
            PlaybookTags tags = gson.fromJson(JSON.toJSONString(result), PlaybookTags.class);
            return tags;
        } catch (Exception e) {
            return new PlaybookTags();
        }
    }
}
