package com.baiyi.caesar.util;

import com.baiyi.caesar.common.builder.SimpleDict;
import com.baiyi.caesar.common.builder.CredentialTemplateDictBuilder;
import com.baiyi.caesar.domain.generator.caesar.Credential;
import org.apache.commons.text.StringSubstitutor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/3 5:32 下午
 * @Version 1.0
 */
@Component
public class TemplateUtil {

    @Resource
    private StringEncryptor stringEncryptor;

    public interface NAMES {
        String USERNAME = "credentialUsername";
        String PASSWORD = "credentialPassword";
        String TOKEN = "credentialToken";
    }

    public String renderTemplate(String propsYml, Credential credential) {
        String password = decrypt(credential.getCredential());
        SimpleDict dict = CredentialTemplateDictBuilder.newBuilder()
                .paramEntry(NAMES.USERNAME, credential.getUsername())
                .paramEntry(NAMES.PASSWORD, password)
                .paramEntry(NAMES.TOKEN, password)
                .build();
        return renderTemplate(propsYml, dict.getDict());
    }

    private String decrypt(String str) {
        if (StringUtils.isEmpty(str)) return null;
        return stringEncryptor.decrypt(str);
    }

    private String renderTemplate(String templateString, Map<String, String> variable) {
        try {
            StringSubstitutor sub = new StringSubstitutor(variable);
            return sub.replace(templateString);
        } catch (Exception e) {
            return templateString;
        }
    }

}
