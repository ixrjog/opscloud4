package com.baiyi.opscloud.facade.sys;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.terminal.TerminalSessionParam;
import com.baiyi.opscloud.domain.vo.terminal.TerminalSessionVO;

/**
 * @Author baiyi
 * @Date 2021/7/22 4:30 下午
 * @Version 1.0
 */
public interface TerminalSessionFacade {

    DataTable<TerminalSessionVO.Session> queryTerminalSessionPage(TerminalSessionParam.TerminalSessionPageQuery pageQuery);
}
