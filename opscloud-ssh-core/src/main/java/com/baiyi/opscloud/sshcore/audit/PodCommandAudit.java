package com.baiyi.opscloud.sshcore.audit;

import com.baiyi.opscloud.sshcore.audit.base.AbstractCommandAudit;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/7/28 4:29 下午
 * @Version 1.0
 */
@Component
public class PodCommandAudit extends AbstractCommandAudit {

    // fix 匹配\u001b 0次或1次
    private static final String INPUT_REGEX = ".*# \\u001b?.*";

    // ".?\b\\u001b\\[J"
    private static final String BS_REGEX = ".*# \\u001b?[\\[J]?";

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