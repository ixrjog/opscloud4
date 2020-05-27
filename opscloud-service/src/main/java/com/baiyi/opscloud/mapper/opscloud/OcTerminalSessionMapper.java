package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.domain.param.term.TermSessionParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcTerminalSessionMapper extends Mapper<OcTerminalSession> {

    List<OcTerminalSession> queryTerminalSessionByParam(TermSessionParam.PageQuery pageQuery);
}