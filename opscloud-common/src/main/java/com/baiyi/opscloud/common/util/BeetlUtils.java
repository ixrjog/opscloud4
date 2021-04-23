package com.baiyi.opscloud.common.util;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

import java.io.IOException;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/8/14 6:34 下午
 * @Version 1.0
 */
public class BeetlUtils {

    private BeetlUtils() {
    }

    /**
     * 渲染模版
     *
     * @param template
     * @param contentMap
     * @return
     * @throws IOException
     */
    public static String renderTemplate(String template, Map<String, Object> contentMap) throws IOException {
        //初始化代码
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        //获取模板
        Template t = gt.getTemplate(template);
        contentMap.forEach(t::binding);
        // t.binding("name", "beetl");
        //渲染结果
        return t.render();
    }
}
