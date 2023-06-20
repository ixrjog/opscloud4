package com.baiyi.opscloud.test;

import com.baiyi.opscloud.BaseUnit;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * @Author baiyi
 * @Date 2023/6/19 10:59
 * @Version 1.0
 */
public class YmlTest extends BaseUnit {

    @Test
    void test() {

//        Map<String, List<String>> gitFlow = Maps.newHashMap();
//
//        gitFlow.put("prod", Lists.newArrayList("master","release"));
//        gitFlow.put("def", Lists.newArrayList("*"));
//
//
//        GitLabConfig.GitLab gitlab =  new GitLabConfig.GitLab();
//
//        gitlab.setGitFlow(gitFlow);
//        gitlab.setUrl("https://gitlab.chuanyinet.com");
//        gitlab.setToken("AAAABBBB");

     //   print(dump( gitlab));
    }

    public String dump(Object obj) throws JsonSyntaxException {
        DumperOptions dumperOptions = new DumperOptions();
        // 设置层级显示
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        // 显示开始结束分隔符
        dumperOptions.setExplicitStart(true);
        dumperOptions.setExplicitEnd(true);
        // 缩进
        dumperOptions.setIndent(2);

        Yaml yaml = new Yaml(dumperOptions);
        return yaml.dump(obj);
    }


}
