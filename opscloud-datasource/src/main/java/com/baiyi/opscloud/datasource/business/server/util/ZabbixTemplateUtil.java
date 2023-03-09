package com.baiyi.opscloud.datasource.business.server.util;

import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixTemplate;
import com.google.common.collect.Sets;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/12/30 3:06 PM
 * @Version 1.0
 */
public class ZabbixTemplateUtil {

    private ZabbixTemplateUtil() {
    }

    public static boolean hostTemplateEquals(List<ZabbixTemplate.Template> templates, ServerProperty.Server property) {
        if (CollectionUtils.isEmpty(templates)) {
            return CollectionUtils.isEmpty(property.getZabbix().getTemplates());
        } else {
            if (property.getZabbix() == null || CollectionUtils.isEmpty(property.getZabbix().getTemplates())) {
                return false;
            } else {
                Set<String> templateNamSet = Sets.newHashSet();
                templates.forEach(t -> templateNamSet.add(t.getName()));
                for (String template : property.getZabbix().getTemplates()) {
                    if (templateNamSet.contains(template)) {
                        templateNamSet.remove(template);
                    } else {
                        return false;
                    }
                }
                return CollectionUtils.isEmpty(templateNamSet);
            }
        }
    }

}
