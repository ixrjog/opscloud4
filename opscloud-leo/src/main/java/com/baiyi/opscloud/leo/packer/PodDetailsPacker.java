package com.baiyi.opscloud.leo.packer;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployingVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/12/7 16:37
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class PodDetailsPacker {

    @AgoWrapper(extend = true)
    public void wrap(LeoDeployingVO.PodDetails podDetails) {
    }

}