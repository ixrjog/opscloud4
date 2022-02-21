package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.sys.InstanceVO;
import com.baiyi.opscloud.domain.vo.sys.SystemVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.util.SystemInfoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/9/3 6:24 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class RegisteredInstancePacker implements IWrapper<InstanceVO.RegisteredInstance> {

    private final RedisUtil redisUtil;

    @Override
    public void wrap(InstanceVO.RegisteredInstance registeredInstance, IExtend iExtend) {
        if (!iExtend.getExtend()) {
            return;
        }
        String key = SystemInfoUtil.buildKey(registeredInstance);
        if (redisUtil.hasKey(key)) {
            registeredInstance.setSystemInfo((SystemVO.Info) redisUtil.get(key));
        }
    }

}
