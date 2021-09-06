package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Instance;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.sys.InstanceVO;
import com.baiyi.opscloud.domain.vo.sys.SystemVO;
import com.baiyi.opscloud.packer.base.IPacker;
import com.baiyi.opscloud.util.SystemInfoUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/9/3 6:24 下午
 * @Version 1.0
 */
@Component
public class RegisteredInstancePacker implements IPacker<InstanceVO.RegisteredInstance, Instance> {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public InstanceVO.RegisteredInstance toVO(Instance instance) {
        return BeanCopierUtil.copyProperties(instance, InstanceVO.RegisteredInstance.class);
    }

    public InstanceVO.RegisteredInstance wrapToVO(Instance instance, IExtend iExtend) {
        InstanceVO.RegisteredInstance vo = toVO(instance);
        if (iExtend.getExtend()) {
            wrap(vo);
        }
        return vo;
    }

    private void wrap(InstanceVO.RegisteredInstance registeredInstance) {
        String key = SystemInfoUtil.buildKey(registeredInstance);
        if (redisUtil.hasKey(key)) {
            registeredInstance.setSystemInfo((SystemVO.Info) redisUtil.get(key));
        }
    }

}
