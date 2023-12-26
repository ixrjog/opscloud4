package com.baiyi.opscloud.core.util;

import com.baiyi.opscloud.common.builder.CredentialTemplateDictBuilder;
import com.baiyi.opscloud.common.builder.SimpleDict;
import com.baiyi.opscloud.common.constants.enums.CredentialKindEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/7/3 5:32 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TemplateUtil {

    private final StringEncryptor stringEncryptor;

    private static final String CREDENTIAL = "credential";

    public interface Names {
        String USERNAME = CREDENTIAL + "Username";
        String PASSWORD = CREDENTIAL + "Password";
        String TOKEN = CREDENTIAL + "Token";
        String ACCESS_KEY = CREDENTIAL + "AccessKey";
        String SECRET = CREDENTIAL + "Secret";
    }

    /**
     * 渲染模板
     *
     * @param propsYml   YAML
     * @param credential
     * @return
     */
    public String renderTemplate(String propsYml, Credential credential) {
        SimpleDict dict = buildDict(credential);
        if (credential.getKind() == CredentialKindEnum.ACCESS_KEY.getKind()) {
            String credential2 = decrypt(credential.getCredential2());
            dict.put(Names.SECRET, credential2);
        }
        return renderTemplate(propsYml, dict.getDict());
    }

    private SimpleDict buildDict(Credential credential) {
        String decryptedCredential = decrypt(credential.getCredential());
        return CredentialTemplateDictBuilder.newBuilder()
                .putParam(Names.USERNAME, credential.getUsername())
                .putParam(Names.PASSWORD, decryptedCredential)
                .putParam(Names.TOKEN, decryptedCredential)
                .putParam(Names.ACCESS_KEY, decryptedCredential)
                .build();
    }

    private String decrypt(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
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