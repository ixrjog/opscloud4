package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.terminal.TerminalSessionVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.packer.user.UserPacker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/7/22 4:52 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TerminalSessionPacker implements IWrapper<TerminalSessionVO.Session> {

    private final UserPacker userPacker;

    private final TerminalSessionInstancePacker sessionInstancePacker;

    @Override
    public void wrap(TerminalSessionVO.Session session, IExtend iExtend) {
        if (iExtend.getExtend()) {
            userPacker.wrap(session, SimpleExtend.EXTEND);
            sessionInstancePacker.wrap(session, iExtend);
        }
    }

}
