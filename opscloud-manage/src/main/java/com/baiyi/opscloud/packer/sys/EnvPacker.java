package com.baiyi.opscloud.packer.sys;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/5/25 4:43 下午
 * @Version 1.0
 */
@Component
public class EnvPacker {

    /**
     * 不建议使用，推荐 @EnvWrapper
     * @param iEnv
     */
    @Deprecated
    @EnvWrapper(extend = true)
    public void wrap(EnvVO.IEnv iEnv) {
    }

}
