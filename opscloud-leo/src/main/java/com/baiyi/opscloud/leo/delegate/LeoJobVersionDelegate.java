package com.baiyi.opscloud.leo.delegate;

import com.baiyi.opscloud.common.annotation.EnvWrapper;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVersionVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/2/23 13:41
 * @Version 1.0
 */
@Component
public class LeoJobVersionDelegate {

    @EnvWrapper(extend = true)
    public void wrap(LeoJobVersionVO.JobVersion jobVersion){
    }

}