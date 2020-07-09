package com.baiyi.opscloud.decorator.terminal;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.common.util.bae64.FileSizeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSessionInstance;
import com.baiyi.opscloud.domain.vo.term.TerminalSessionInstanceVO;
import com.baiyi.opscloud.xterm.config.XTermConfig;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/25 9:35 下午
 * @Version 1.0
 */
@Component
public class TerminalSessionInstanceDecorator {

    @Resource
    private XTermConfig xtermConfig;

    public TerminalSessionInstanceVO.TerminalSessionInstance decorator(OcTerminalSessionInstance ocTerminalSessionInstance, Integer extend) {
        TerminalSessionInstanceVO.TerminalSessionInstance terminalSessionInstance
                = BeanCopierUtils.copyProperties(ocTerminalSessionInstance, TerminalSessionInstanceVO.TerminalSessionInstance.class);
        String path = xtermConfig.getAuditLogPath(ocTerminalSessionInstance.getSessionId(), terminalSessionInstance.getInstanceId());
        String content = IOUtils.readFile(path);
        TerminalSessionInstanceVO.AuditLog auditLog = new TerminalSessionInstanceVO.AuditLog();
        if (StringUtils.isEmpty(content)) {
            auditLog.setIsEmpty(true);
        } else {
            auditLog.setPath(path);
            if (extend == 1) {
                auditLog.setContent(convert(content));
            }
            auditLog.setIsEmpty(false);
        }
        terminalSessionInstance.setAuditLog(auditLog);
        terminalSessionInstance.setOutputFileSize(FileSizeUtils.formetFileSize(terminalSessionInstance.getOutputSize()));
        return terminalSessionInstance;
    }

    /**
     * 过滤掉特殊字符（终端颜色）, 转换退格
     *
     * @param auditLog
     * @return
     */
    private String convert(String auditLog) {
        //auditLog = auditLog.replaceAll("\\u0007|\u001B\\[K|\\]0;|\\[\\d\\d;\\d\\dm|\\[\\dm", "");
        auditLog = auditLog.replaceAll("\r\n\r\n", "\r\n \r\n");
        while (auditLog.contains("\b")) {
            auditLog = auditLog.replaceFirst(".\b", ""); // 退格处理
        }
        return auditLog;
    }
}
