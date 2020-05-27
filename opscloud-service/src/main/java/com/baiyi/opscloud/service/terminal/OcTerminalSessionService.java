package com.baiyi.opscloud.service.terminal;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.domain.param.term.TermSessionParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/24 12:18 下午
 * @Version 1.0
 */
public interface OcTerminalSessionService {

    DataTable<OcTerminalSession> queryTerminalSessionByParam(TermSessionParam.PageQuery pageQuery);

    OcTerminalSession queryOcTerminalSessionBySessionId(String sessionId);

    List<OcTerminalSession> queryOcTerminalSessionByActive();

    void addOcTerminalSession(OcTerminalSession ocTerminalSession);

    void updateOcTerminalSession(OcTerminalSession ocTerminalSession);
}
