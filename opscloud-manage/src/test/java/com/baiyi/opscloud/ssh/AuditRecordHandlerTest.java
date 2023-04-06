package com.baiyi.opscloud.ssh;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstanceCommand;
import com.baiyi.opscloud.sshcore.config.TerminalConfigurationProperties;
import com.baiyi.opscloud.sshcore.audit.ServerCommandAudit;
import com.baiyi.opscloud.sshcore.audit.InstanceCommandBuilder;
import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.regex.Pattern;

/**
 * @Author baiyi
 * @Date 2021/7/27 6:45 下午
 * @Version 1.0
 */
public class AuditRecordHandlerTest extends BaseUnit {

    @Resource
    private ServerCommandAudit auditCommandHandler;

    @Resource
    private TerminalConfigurationProperties terminalConfig;

    private static final String REGEX = "\\u001b.*\\$?";

    @Test
    void dddddd() {
        String cmd = "\u001B]0;xincheng@account-gray-1:~\u0007[xincheng@account-gray-1 ~]$ pwd";
        boolean isInput = Pattern.matches(".*]?[$|#].*", cmd);
        System.err.println(isInput);
    }


    @Test
    void podCommandTest() {
        testPodLog("06806244-f90f-4e19-8a1d-55f5b0ebc6a2", "account-dev-deployment-785f5657df-nj6t7#DEFAULT_CONTAINER#4558a7dabeb8420a952e1838bc6cf5e8");
        print("-------------------------------------");
        testPodLog("28362479-a559-46a2-a303-9a53a1b8b869", "merchant-gateway-dev-74fccbc689-hnr5b#merchant-gateway-dev");
    }

    // \u001b
    // private static final String INPUT_REGEX = "#";

    private static final String BS_REGEX = ".?\b\\u001b\\[J";

    private void testPodLog(String sessionId, String instanceId) {
        String path = terminalConfig.buildAuditLogPath(sessionId, instanceId);
        String str;
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(path));
            while ((str = reader.readLine()) != null) {
                if (!str.isEmpty()) {
                    boolean isInput = Pattern.matches(".*# \\u001b?.*", str);
                    if (isInput) {
                        print("匹配行 : " + str);
                        String cmd = str.replaceFirst(".*# \\u001b?[\\[J]?", "");
                        print("取命令 : " + cmd);
                    } else {
                        print("未匹配到行 : " + str);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // InstanceCommandBuilder

    private void formatCommanderLog(String sessionId, String instanceId, Integer terminalSessionInstanceId) {
        String commanderLogPath = terminalConfig.buildAuditLogPath(sessionId, instanceId);
        String str;
        InstanceCommandBuilder builder = null;
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(commanderLogPath));
            while ((str = reader.readLine()) != null) {
                if (!str.isEmpty()) {
                    boolean isInput = Pattern.matches(REGEX, str);
                    if (isInput) {
                        if (builder == null) {
                            TerminalSessionInstanceCommand command = TerminalSessionInstanceCommand.builder()
                                    .terminalSessionInstanceId(terminalSessionInstanceId)
                                    .prompt("")
                                    .input(str)
                                    .build();
                            builder = InstanceCommandBuilder.newBuilder(command);
                        } else {

                        }
                        print("匹配行 : " + str);
                    } else {
                        print("未匹配到行 : " + str);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void formatCommanderLog2(String sessionId, String instanceId) {
        String commanderLogPath = terminalConfig.buildAuditLogPath(sessionId, instanceId);
        String fmtCommanderLogPath = terminalConfig.buildFmtCommanderLogPath(sessionId, instanceId);
        String str;


        AsciiString as = new AsciiString(new char[]{127});
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(commanderLogPath));
            while ((str = reader.readLine()) != null) {
                if (!str.isEmpty()) {
                    print(str);
                    str = str.replaceAll("\\p{C}", "");
                    //   str = str.replaceFirst(".?\\x7f", ""); // 退格处理
                    print(str);
                    // IOUtil.appendFile(str + "\r\n", fmtCommanderLogPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
