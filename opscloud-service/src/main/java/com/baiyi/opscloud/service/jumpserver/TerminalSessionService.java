package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.jumpserver.TerminalSession;
import com.baiyi.opscloud.domain.param.PageParam;

/**
 * @Author baiyi
 * @Date 2020/3/13 6:08 下午
 * @Version 1.0
 */
public interface TerminalSessionService {

    /**
     * 查询终端的当前会话
     * @param terminalId
     * @return
     */
    int countTerminalSession(String terminalId);

    DataTable<TerminalSession> queryTerminalSessionPage(PageParam pageQuery);
}
