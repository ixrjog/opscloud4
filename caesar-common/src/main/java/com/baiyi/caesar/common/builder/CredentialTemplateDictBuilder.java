package com.baiyi.caesar.common.builder;

import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2021/5/18 2:44 下午
 * @Version 1.0
 */
public class CredentialTemplateDictBuilder {

    private CommonDict credentialTemplateDict = new CommonDict();

    private CredentialTemplateDictBuilder() {
    }

    static public CredentialTemplateDictBuilder newBuilder() {
        return new CredentialTemplateDictBuilder();
    }

    public CredentialTemplateDictBuilder paramEntry(String name, String value) {
        if (!StringUtils.isEmpty(value))
            credentialTemplateDict.put(name, value);
        return this;
    }

    public CommonDict build() {
        return credentialTemplateDict;
    }
}
