package com.baiyi.opscloud.service.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.Terminal;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/13 5:56 下午
 * @Version 1.0
 */
public interface TerminalService {

    /**
     * 查询正在使用的Terminal(koko)
     * @return
     */
    List<Terminal> queryTerminal();
}
