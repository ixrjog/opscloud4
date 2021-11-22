package com.baiyi.opscloud.sshserver.command.event.util;

import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.zabbix.constant.SeverityType;

/**
 * @Author baiyi
 * @Date 2021/10/13 4:25 下午
 * @Version 1.0
 */
public class SeverityUtil {

    private SeverityUtil() {
    }

    /**
     * DEFAULT(0, "DEFAULT"),
     * INFORMATION(1, "INFORMATION"),
     * WARNING(2, "WARNING"),
     * AVERAGE(3, "AVERAGE"),
     * HIGH(4, "HIGH"),
     * DISASTER(5, "DISASTER");
     *
     * @param priority
     * @return
     */
    public static String toTerminalStr(int priority) {
        String severity = SeverityType.getName(priority);
        switch (priority) {
            case 5:
                return SshShellHelper.getColoredMessage(severity, PromptColor.MAGENTA);
            case 4:
            case 3:
                return SshShellHelper.getColoredMessage(severity, PromptColor.RED);
            case 2:
                return SshShellHelper.getColoredMessage(severity, PromptColor.YELLOW);
            case 1:
                return SshShellHelper.getColoredMessage(severity, PromptColor.GREEN);
        }
        return severity;
    }


}
