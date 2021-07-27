package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.terminal.TerminalSessionVO;
import com.baiyi.opscloud.packer.user.UserPacker;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/7/22 4:52 下午
 * @Version 1.0
 */
@Component
public class TerminalSessionPacker {

    @Resource
    private UserPacker userPacker;

    @Resource
    private TerminalSessionInstancePacker sessionInstancePacker;

    public List<TerminalSessionVO.Session> wrapVOList(List<TerminalSession> data, IExtend iExtend) {
        return data.stream().map(e ->
                wrapVO(e, iExtend)
        ).collect(Collectors.toList());
    }

    public TerminalSessionVO.Session wrapVO(TerminalSession terminalSession, IExtend iExtend) {
        TerminalSessionVO.Session session = BeanCopierUtil.copyProperties(terminalSession, TerminalSessionVO.Session.class);
        if (iExtend.getExtend()) {
            userPacker.wrap(session);
            sessionInstancePacker.wrapVO(session);
        }
        return session;
    }

}
