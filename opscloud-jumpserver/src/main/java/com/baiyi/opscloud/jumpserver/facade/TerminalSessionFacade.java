package com.baiyi.opscloud.jumpserver.facade;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverTerminalSessionVO;

/**
 * @Author baiyi
 * @Date 2020/3/16 10:54 上午
 * @Version 1.0
 */
public interface TerminalSessionFacade {

    DataTable<JumpserverTerminalSessionVO.TerminalSession> queryTerminalSessionPage(PageParam pageQuery);
}
