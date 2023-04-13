package com.baiyi.opscloud.sshcore.audit;

import com.baiyi.opscloud.sshcore.audit.base.AbstractCommandAudit;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/7/28 11:32 上午
 * @Version 1.0
 */
@Component
public class ServerCommandAudit extends AbstractCommandAudit {

    // private static final String INPUT_REGEX = "\\u001b.*\\[\\$|#]?";

    private static final String INPUT_REGEX = ".*\\][$|#].*";

    private static final String BS_REGEX = ".?\b\\u001b\\[J";

    @Override
    protected String getInputRegex() {
        return INPUT_REGEX;
    }

    @Override
    protected String getBsRegex() {
        return BS_REGEX;
    }

}