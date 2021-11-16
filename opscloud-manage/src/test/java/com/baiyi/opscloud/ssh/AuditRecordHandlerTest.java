package com.baiyi.opscloud.ssh;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstanceCommand;
import com.baiyi.opscloud.sshcore.config.TerminalConfigurationProperties;
import com.baiyi.opscloud.sshcore.audit.AuditServerCommandAudit;
import com.baiyi.opscloud.sshcore.audit.InstanceCommandBuilder;
import io.netty.util.AsciiString;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
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
    private AuditServerCommandAudit auditCommandHandler;

    @Resource
    private TerminalConfigurationProperties terminalConfig;

    private static final String REGEX = "\\u001b.*\\$?";

    @Test
    void dddddd(){
        String cmd = "\u001B]0;xincheng@account-gray-1:~\u0007[xincheng@account-gray-1 ~]$ pwd";
        boolean isInput = Pattern.matches(".*]?[$|#].*", cmd);
        System.err.println(isInput);
    }


    @Test
    void ddd(){
        testPodLog("273ce35a-f3a9-4f35-8a68-1a98f275c04d","data-urc-dev-deployment-56bf5cbc77-pl5cx#DEFAULT_CONTAINER#2423457befc448e3b14cb0c2f3f23376");
   }

   // \u001b
   // private static final String INPUT_REGEX = "#";

    private void testPodLog(String sessionId, String instanceId){
      String path =  terminalConfig.buildAuditLogPath(sessionId, instanceId);
        String str;
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(path));
            while ((str = reader.readLine()) != null) {
                if (!str.isEmpty()) {
                    boolean isInput = Pattern.matches(".*# \\u001b.*", str);
                    if (isInput) {
                        System.out.println("匹配行 : " + str);
                    } else {
                        System.out.println("未匹配到行 : " + str);
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
                        }else{

                        }

                        System.out.println("匹配行 : " + str);
                    } else {
                        System.out.println("未匹配到行 : " + str);
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

                        System.err.println(str);
                        str = str.replaceAll("\\p{C}","");
                     //   str = str.replaceFirst(".?\\x7f", ""); // 退格处理
                        System.err.println(str);


                   // IOUtil.appendFile(str + "\r\n", fmtCommanderLogPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
