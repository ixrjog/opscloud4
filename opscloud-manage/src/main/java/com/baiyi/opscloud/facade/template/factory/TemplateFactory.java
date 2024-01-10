package com.baiyi.opscloud.facade.template.factory;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author baiyi
 * @Date 2021/12/7 3:15 PM
 * @Version 1.0
 */
@Slf4j
public class TemplateFactory {

    private TemplateFactory() {
    }

    private static final Table<String, String, ITemplateProvider> context = HashBasedTable.create();

    public static ITemplateProvider getByInstanceAsset(String instanceType, String templateKey) {
        return context.get(instanceType, templateKey);
    }

    public static void register(ITemplateProvider bean) {
        context.put(bean.getInstanceType(), bean.getTemplateKey(), bean);
        log.debug("TemplateFactory Registered: instanceType={}, templateKey={}", bean.getInstanceType(), bean.getTemplateKey());
    }

}