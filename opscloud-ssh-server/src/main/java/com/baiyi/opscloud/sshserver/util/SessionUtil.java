package com.baiyi.opscloud.sshserver.util;

import com.baiyi.opscloud.common.util.HashUtil;
import com.google.common.base.Joiner;
import org.apache.sshd.common.io.IoSession;

/**
 * @Author baiyi
 * @Date 2021/6/8 10:51 上午
 * @Version 1.0
 */
public class SessionUtil {

    public static String buildSessionId(IoSession ioSession) {
        String str = Joiner.on("-").join(ioSession.getId(), ioSession.getLocalAddress(), ioSession.getRemoteAddress());
        return HashUtil.md5(str);
    }

}