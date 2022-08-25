package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.sys.InstanceVO;
import com.baiyi.opscloud.domain.vo.sys.SystemVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.sshcore.enums.SessionTypeEnum;
import com.baiyi.opscloud.util.SystemInfoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/3 6:24 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class RegisteredInstancePacker implements IWrapper<InstanceVO.RegisteredInstance> {

    private final RedisUtil redisUtil;

    private final TerminalSessionService terminalSessionService;

    @Override
    public void wrap(InstanceVO.RegisteredInstance registeredInstance, IExtend iExtend) {
        if (!iExtend.getExtend()) {
            return;
        }
        String key = SystemInfoUtil.buildKey(registeredInstance);
        if (redisUtil.hasKey(key)) {
            registeredInstance.setSystemInfo((SystemVO.Info) redisUtil.get(key));
        }
        registeredInstance.setActiveSessionMap(buildActiveSessionMap(registeredInstance.getHostname()));
    }

    public Map<String, Integer> buildActiveSessionMap(String serverHostname) {
        return Arrays.stream(SessionTypeEnum.values())
                .collect(Collectors.toMap(Enum::name, value -> terminalSessionService.countActiveSessionByParam(serverHostname, value.name()), (a, b) -> b));
    }


}
