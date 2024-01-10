package com.baiyi.opscloud.leo.packer;

import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVersionVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/7/13 09:32
 * @Version 1.0
 */
@Component
public class DeploymentVersionPacker {

    @TagsWrapper(extend = true)
    public void wrap(LeoJobVersionVO.DeploymentVersion deploymentVersion) {
    }

}