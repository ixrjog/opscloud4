package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Instance;
import com.baiyi.opscloud.domain.vo.sys.InstanceVO;
import com.baiyi.opscloud.packer.base.IPacker;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/9/3 6:24 下午
 * @Version 1.0
 */
@Component
public class RegisteredInstancePacker implements IPacker<InstanceVO.RegisteredInstance, Instance> {

    @Override
    public InstanceVO.RegisteredInstance toVO(Instance instance) {
        return BeanCopierUtil.copyProperties(instance, InstanceVO.RegisteredInstance.class);
    }
}
