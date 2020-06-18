package com.baiyi.opscloud.mapper.jumpserver;

import com.baiyi.opscloud.domain.generator.jumpserver.TerminalSession;
import com.baiyi.opscloud.domain.param.PageParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TerminalSessionMapper extends Mapper<TerminalSession> {

    List<TerminalSession> queryTerminalSessionPage(PageParam pageQuery);
}