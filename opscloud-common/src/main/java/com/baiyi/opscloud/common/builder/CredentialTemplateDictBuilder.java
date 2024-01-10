package com.baiyi.opscloud.common.builder;

import org.apache.commons.lang3.StringUtils;
/**
 * @Author baiyi
 * @Date 2021/5/18 2:44 下午
 * @Version 1.0
 */
public class CredentialTemplateDictBuilder {

    private final SimpleDict credentialTemplateDict = new SimpleDict();

    private CredentialTemplateDictBuilder() {
    }

    static public CredentialTemplateDictBuilder newBuilder() {
        return new CredentialTemplateDictBuilder();
    }

    public CredentialTemplateDictBuilder putParam(String name, String value) {
        if (!StringUtils.isEmpty(value)) {
            credentialTemplateDict.put(name, value);
        }
        return this;
    }

    public SimpleDict build() {
        return credentialTemplateDict;
    }

}