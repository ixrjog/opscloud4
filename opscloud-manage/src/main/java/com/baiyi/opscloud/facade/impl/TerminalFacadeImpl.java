package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.decorator.TerminalSessionDecorator;
import com.baiyi.opscloud.decorator.TerminalSessionInstanceDecorator;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSessionInstance;
import com.baiyi.opscloud.domain.param.term.TermSessionParam;
import com.baiyi.opscloud.domain.vo.term.TerminalSessionInstanceVO;
import com.baiyi.opscloud.domain.vo.term.TerminalSessionVO;
import com.baiyi.opscloud.facade.TerminalFacade;
import com.baiyi.opscloud.service.terminal.OcTerminalSessionInstanceService;
import com.baiyi.opscloud.service.terminal.OcTerminalSessionService;
import com.baiyi.opscloud.xterm.handler.AuditLogHandler;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/5/25 4:30 下午
 * @Version 1.0
 */
@Service
public class TerminalFacadeImpl implements TerminalFacade {

    @Resource
    private OcTerminalSessionService ocTerminalSessionService;

    @Resource
    private OcTerminalSessionInstanceService ocTerminalSessionInstanceService;

    @Resource
    private TerminalSessionDecorator terminalSessionDecorator;

    @Resource
    private TerminalSessionInstanceDecorator terminalSessionInstanceDecorator;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public DataTable<TerminalSessionVO.TerminalSession> queryTerminalSessionPage(TermSessionParam.PageQuery pageQuery) {
        DataTable<OcTerminalSession> table = ocTerminalSessionService.queryTerminalSessionByParam(pageQuery);
        DataTable<TerminalSessionVO.TerminalSession> dataTable
                = new DataTable<>(table.getData().stream().map(e -> terminalSessionDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
        return dataTable;
    }

    @Override
    public TerminalSessionInstanceVO.TerminalSessionInstance querySessionInstanceById(int id) {
        OcTerminalSessionInstance ocTerminalSessionInstance = ocTerminalSessionInstanceService.queryOcTerminalSessionInstanceById(id);
        return terminalSessionInstanceDecorator.decorator(ocTerminalSessionInstance, 1);
    }

    @Override
    public void closeInvalidSession() {
        List<OcTerminalSession> list = ocTerminalSessionService.queryOcTerminalSessionByActive();
        list.forEach(e -> {
            String key = Joiner.on("#").join(e.getSessionId(), "heartbeat");
            if (!redisUtil.hasKey(key)) {
                List<OcTerminalSessionInstance> instanceList = ocTerminalSessionInstanceService.queryOcTerminalSessionInstanceBySessionId(e.getSessionId());
                instanceList.forEach(i -> {
                    AuditLogHandler.writeAuditLog(e.getSessionId(), i.getInstanceId());
                    i.setIsClosed(true);
                    i.setCloseTime(new Date());
                    ocTerminalSessionInstanceService.updateOcTerminalSessionInstance(i);
                });
                e.setIsClosed(true);
                e.setCloseTime(new Date());
                ocTerminalSessionService.updateOcTerminalSession(e);
            }
        });
    }

}
