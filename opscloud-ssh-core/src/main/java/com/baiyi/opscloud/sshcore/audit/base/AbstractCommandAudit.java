package com.baiyi.opscloud.sshcore.audit.base;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstanceCommand;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceCommandService;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import com.baiyi.opscloud.sshcore.audit.InstanceCommandBuilder;
import com.baiyi.opscloud.sshcore.config.TerminalConfigurationProperties;
import com.baiyi.opscloud.sshcore.enums.InstanceSessionTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.regex.Pattern;

/**
 * @Author baiyi
 * @Date 2021/7/28 4:31 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractCommandAudit {

    @Resource
    private TerminalConfigurationProperties terminalConfig;

    @Resource
    private TerminalSessionInstanceService terminalSessionInstanceService;

    @Resource
    private TerminalSessionInstanceCommandService terminalSessionInstanceCommandService;

    protected abstract String getInputRegex();

    protected abstract String getBsRegex();

    /**
     * 记录命令 inpunt & output
     *
     * @param sessionId
     * @param instanceId
     */
    @Async
    public void asyncRecordCommand(String sessionId, String instanceId) {
        TerminalSessionInstance terminalSessionInstance = terminalSessionInstanceService.getByUniqueKey(sessionId, instanceId);
        // 跳过日志审计
        if (InstanceSessionTypeEnum.CONTAINER_LOG.getType().equals(terminalSessionInstance.getInstanceSessionType())) {
            return;
        }
        final String commanderLogPath = terminalConfig.buildAuditLogPath(sessionId, instanceId);
        String str;
        InstanceCommandBuilder builder = null;
        String regex = getInputRegex();
        File file = new File(commanderLogPath);
        if (!file.exists()) {
            log.debug("命令审计文件不存在: sessionId={}, instanceId={}, path={}", sessionId, instanceId, commanderLogPath);
            return;
        }
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(commanderLogPath));
            while ((str = reader.readLine()) != null) {
                if (!str.isEmpty()) {
                    boolean isInput = Pattern.matches(regex, str);
                    if (isInput) {
                        if (builder != null) {
                            // save
                            TerminalSessionInstanceCommand auditCommand = builder.build();
                            if (auditCommand != null) {
                                if (!StringUtils.isEmpty(auditCommand.getInputFormatted())) {
                                    terminalSessionInstanceCommandService.add(auditCommand);
                                }
                                builder = null;
                            }
                        }
                        builder = builder(terminalSessionInstance.getId(), str);
                    } else {
                        if (builder != null) {
                            builder.addOutput(str);
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("写入命令审计文件错误: sessionId={}, instanceId={}, err={}", sessionId, instanceId, e.getMessage());
        }
    }

    /**
     * 同步接口
     *
     * @param instance
     */
    public void recordCommand(TerminalSessionInstance instance) {
        try {
            this.asyncRecordCommand(instance.getSessionId(), instance.getInstanceId());
        } catch (Exception e) {
            log.error("记录日志错误: sessionId={}, instanceId={}", instance.getSessionId(), instance.getInstanceId());
        }
    }

    private ImmutablePair<Integer, Integer> getIndex(String inputStr) {
        int index1 = inputStr.indexOf("$");
        int index2 = inputStr.indexOf("#");
        if (index1 >= index2) {
            return ImmutablePair.of(index2, index1);
        }
        return ImmutablePair.of(index1, index2);
    }

    public InstanceCommandBuilder builder(Integer terminalSessionInstanceId, String inputStr) {
        ImmutablePair<Integer, Integer> ip = getIndex(inputStr);
        int index = ip.getLeft() != -1 ? ip.getLeft() : ip.getRight();
        if (index == -1) {
            return null;
        }
        TerminalSessionInstanceCommand command = TerminalSessionInstanceCommand.builder()
                .terminalSessionInstanceId(terminalSessionInstanceId)
                .prompt(inputStr.substring(0, index + 1))
                // 取用户输入
                .input(inputStr.length() > index + 2 ? inputStr.substring(index + 2) : "")
                .build();
        formatInput(command);
        return InstanceCommandBuilder.newBuilder(command);
    }

    public void formatInput(TerminalSessionInstanceCommand command) {
        String input = command.getInput();
        while (input.contains("\b")) {
            // 退格处理
            String ni = input.replaceFirst(getBsRegex(), "");
            // 避免死循环
            if (ni.equals(input)) {
                ni = input.replaceFirst(".?\b", "");
            }
            input = ni;
        }
        String inputFormatted = eraseInvisibleCharacters(input);
        command.setInputFormatted(inputFormatted);
        command.setIsFormatted(true);
    }

    /**
     * 删除所有不可见字符（但不包含退格）
     *
     * @param input
     * @return
     */
    protected String eraseInvisibleCharacters(String input) {
        return input.replaceAll("\\p{C}", "");
    }

}