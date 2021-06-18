package com.baiyi.caesar.datasource.util;

import com.baiyi.caesar.common.builder.CredentialTemplateDictBuilder;
import com.baiyi.caesar.common.builder.SimpleDict;
import com.baiyi.caesar.common.type.CredentialKindEnum;
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

    public interface Names {
        String USERNAME = "credentialUsername";
        String PASSWORD = "credentialPassword";
        String TOKEN = "credentialToken";
        String ACCESS_KEY = "credentialAccessKey";
        String SECRET = "credentialSecret";
    }

    public String renderTemplate(String propsYml, Credential credential) {
        String credential1 = decrypt(credential.getCredential());
        SimpleDict dict = CredentialTemplateDictBuilder.newBuilder()
                .paramEntry(Names.USERNAME, credential.getUsername())
                .paramEntry(Names.PASSWORD, credential1)
                .paramEntry(Names.TOKEN, credential1)
                .paramEntry(Names.ACCESS_KEY, credential1)
                .build();
        if (credential.getKind() == CredentialKindEnum.ACCESS_KEY.getKind()) {
            String credential2 = decrypt(credential.getCredential2());
            dict.put(Names.SECRET, credential2);
        }
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
