package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.term.TermSessionParam;
import com.baiyi.opscloud.domain.vo.term.TerminalSessionInstanceVO;
import com.baiyi.opscloud.domain.vo.term.TerminalSessionVO;

/**
 * @Author baiyi
 * @Date 2020/5/25 4:30 下午
 * @Version 1.0
 */
public interface TerminalFacade {

    DataTable<TerminalSessionVO.TerminalSession> queryTerminalSessionPage(TermSessionParam.PageQuery pageQuery);

    TerminalSessionInstanceVO.TerminalSessionInstance  querySessionInstanceById(int id);

    void closeInvalidSessionTask();
}
