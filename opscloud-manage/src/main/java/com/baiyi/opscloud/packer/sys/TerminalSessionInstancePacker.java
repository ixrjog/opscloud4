package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.terminal.TerminalSessionInstanceVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceCommandService;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/7/22 5:13 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TerminalSessionInstancePacker implements IWrapper<TerminalSessionInstanceVO.SessionInstance> {

    private final TerminalSessionInstanceService terminalSessionInstanceService;

    private final TerminalSessionInstanceCommandService commandService;

    public void wrap(TerminalSessionInstanceVO.ISessionInstances iSessionInstances, IExtend iExtend) {
        List<TerminalSessionInstance> sessionInstances = terminalSessionInstanceService.queryBySessionId(iSessionInstances.getSessionId());
        List<TerminalSessionInstanceVO.SessionInstance> data = BeanCopierUtil.copyListProperties(sessionInstances, TerminalSessionInstanceVO.SessionInstance.class)
                .stream()
                .peek(e -> wrap(e, iExtend))
                .collect(Collectors.toList());
        iSessionInstances.setSessionInstances(data);
    }

    @Override
    public void wrap(TerminalSessionInstanceVO.SessionInstance sessionInstance, IExtend iExtend) {
        // 会话时长
        Date endTime = sessionInstance.getInstanceClosed() ? sessionInstance.getCloseTime() : new Date();
        sessionInstance.setSessionDuration(Long.valueOf((endTime.getTime() - sessionInstance.getOpenTime().getTime()) / 1000).intValue());
        sessionInstance.setCommandSize(commandService.countByTerminalSessionInstanceId(sessionInstance.getId()));
    }

}
