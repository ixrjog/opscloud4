package com.baiyi.opscloud.sshcore.audit;

import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/7/28 4:29 下午
 * @Version 1.0
 */
@Component
public class AuditPodCommandHandler extends AbstractCommandHandler {

    private static final String INPUT_REGEX = ".*# \\u001b.*";

    private static final String BS_REGEX = ".?\b\\u001b\\[J";

    @Override
    protected String getInputRegex() {
        return INPUT_REGEX;
    }

    @Override
    protected String getBsRegex() {
        return BS_REGEX;
    }

    @Override
    protected String eraseInvisibleCharacters(String input) {
        String in = super.eraseInvisibleCharacters(input);
        in = in.replaceAll("\\[6n","");
        return in.replaceAll("\\[J","");
    }

}
