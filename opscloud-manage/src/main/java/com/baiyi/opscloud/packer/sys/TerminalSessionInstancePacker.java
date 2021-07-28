package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.domain.vo.terminal.TerminalSessionInstanceVO;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/7/22 5:13 下午
 * @Version 1.0
 */
@Component
public class TerminalSessionInstancePacker {

    @Resource
    private TerminalSessionInstanceService terminalSessionInstanceService;

    public void wrapVO(TerminalSessionInstanceVO.ISessionInstances iSessionInstances) {
        List<TerminalSessionInstance> sessionInstances = terminalSessionInstanceService.queryBySessionId(iSessionInstances.getSessionId());
        iSessionInstances.setSessionInstances(sessionInstances.stream().map(this::wrapVO).collect(Collectors.toList()));
    }

    public TerminalSessionInstanceVO.SessionInstance wrapVO(TerminalSessionInstance terminalSessionInstance) {
        TerminalSessionInstanceVO.SessionInstance vo = BeanCopierUtil.copyProperties(terminalSessionInstance, TerminalSessionInstanceVO.SessionInstance.class);
        // 会话时长
        Date endTime = vo.getInstanceClosed() ? vo.getCloseTime() : new Date();
        vo.setSessionDuration(Long.valueOf((endTime.getTime() - vo.getOpenTime().getTime()) / 1000).intValue());
        return vo;
    }
}
