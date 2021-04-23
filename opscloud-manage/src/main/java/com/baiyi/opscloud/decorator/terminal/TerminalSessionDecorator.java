package com.baiyi.opscloud.decorator.terminal;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSessionInstance;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.term.TerminalSessionVO;
import com.baiyi.opscloud.service.terminal.OcTerminalSessionInstanceService;
import com.baiyi.opscloud.service.user.OcUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/5/25 4:33 下午
 * @Version 1.0
 */
@Component
public class TerminalSessionDecorator {

    @Resource
    private OcTerminalSessionInstanceService ocTerminalSessionInstanceService;

    @Resource
    private TerminalSessionInstanceDecorator terminalSessionInstanceDecorator;

    @Resource
    private OcUserService ocUserService;

    public TerminalSessionVO.TerminalSession decorator(OcTerminalSession ocTerminalSession, Integer extend) {
        TerminalSessionVO.TerminalSession terminalSession = BeanCopierUtils.copyProperties(ocTerminalSession, TerminalSessionVO.TerminalSession.class);
        if (extend == 1) {
            List<OcTerminalSessionInstance> instanceList = ocTerminalSessionInstanceService.queryOcTerminalSessionInstanceBySessionId(ocTerminalSession.getSessionId());
            terminalSession.setSessionInstances(
                    instanceList.stream().map(i ->
                            terminalSessionInstanceDecorator.decorator(i, 0)
                    ).collect(Collectors.toList()));
            OcUser ocUser = ocUserService.queryOcUserByUsername(terminalSession.getUsername());
            terminalSession.setOcUser(ocUser);
        }
        return terminalSession;
    }


}
