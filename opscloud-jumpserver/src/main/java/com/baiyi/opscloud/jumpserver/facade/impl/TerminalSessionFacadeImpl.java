package com.baiyi.opscloud.jumpserver.facade.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.jumpserver.TerminalSession;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverTerminalSessionVO;
import com.baiyi.opscloud.jumpserver.facade.TerminalSessionFacade;
import com.baiyi.opscloud.service.jumpserver.TerminalSessionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/16 10:55 上午
 * @Version 1.0
 */
@Service
public class TerminalSessionFacadeImpl implements TerminalSessionFacade{

    @Resource
    private TerminalSessionService terminalSessionService;

    @Override
    public DataTable<JumpserverTerminalSessionVO.TerminalSession> queryTerminalSessionPage(PageParam pageQuery) {
        DataTable<TerminalSession> table = terminalSessionService.queryTerminalSessionPage(pageQuery);
        List<JumpserverTerminalSessionVO.TerminalSession> page = BeanCopierUtils.copyListProperties(table.getData(), JumpserverTerminalSessionVO.TerminalSession.class);
        return  new DataTable<>(page, table.getTotalNum());
    }
}
