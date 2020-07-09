package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.bae64.CacheKeyUtils;
import com.baiyi.opscloud.decorator.terminal.TerminalSessionDecorator;
import com.baiyi.opscloud.decorator.terminal.TerminalSessionInstanceDecorator;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/5/25 4:30 下午
 * @Version 1.0
 */
@Slf4j
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
        return new DataTable<>(table.getData().stream().map(e -> terminalSessionDecorator.decorator(e, pageQuery.getExtend())).collect(Collectors.toList()), table.getTotalNum());
    }

    @Override
    public TerminalSessionInstanceVO.TerminalSessionInstance querySessionInstanceById(int id) {
        OcTerminalSessionInstance ocTerminalSessionInstance = ocTerminalSessionInstanceService.queryOcTerminalSessionInstanceById(id);
        return terminalSessionInstanceDecorator.decorator(ocTerminalSessionInstance, 1);
    }

    @Override
    public void closeInvalidSessionTask() {
        List<OcTerminalSession> list = ocTerminalSessionService.queryOcTerminalSessionByActive();
        list.forEach(e -> {
            log.info("扫描会话 sessionId = {} ！", e.getSessionId());
            String key = CacheKeyUtils.getTermSessionHeartbeatKey(e.getSessionId());
            if (!redisUtil.hasKey(key)) {
                List<OcTerminalSessionInstance> instanceList = ocTerminalSessionInstanceService.queryOcTerminalSessionInstanceBySessionId(e.getSessionId());
                if (!CollectionUtils.isEmpty(instanceList))
                    instanceList.forEach(i -> closeInvalidSessionInstance(e.getSessionId(), i));
                closeInvalidSession(e);
            } else {
                log.info("会话 sessionId = {} 心跳存在！", e.getSessionId());
            }
        });
    }

    private void closeInvalidSession(OcTerminalSession ocTerminalSession) {
        ocTerminalSession.setIsClosed(true);
        ocTerminalSession.setCloseTime(new Date());
        ocTerminalSessionService.updateOcTerminalSession(ocTerminalSession);
    }

    private void closeInvalidSessionInstance(String sessionId, OcTerminalSessionInstance ocTerminalSessionInstance) {
        if (ocTerminalSessionInstance.getIsClosed()) return;
        log.info("会话 sessionId = {} , 实例 instanceId = {} , 心跳丢失尝试写入日志！", sessionId, ocTerminalSessionInstance.getInstanceId());
        AuditLogHandler.writeAuditLog(sessionId, ocTerminalSessionInstance.getInstanceId());
        ocTerminalSessionInstance.setIsClosed(true);
        ocTerminalSessionInstance.setCloseTime(new Date());
        ocTerminalSessionInstanceService.updateOcTerminalSessionInstance(ocTerminalSessionInstance);
    }

}
