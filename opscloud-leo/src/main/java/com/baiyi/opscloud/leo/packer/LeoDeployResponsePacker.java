package com.baiyi.opscloud.leo.packer;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.common.annotation.RuntimeWrapper;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployVO;
import com.baiyi.opscloud.leo.domain.model.LeoDeployModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/12/6 20:02
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoDeployResponsePacker {

    @AgoWrapper(extend = true)
    @RuntimeWrapper(extend = true)
    public void wrap(LeoDeployVO.Deploy deploy) {
        LeoDeployModel.DeployConfig deployConfig = LeoDeployModel.load(deploy.getDeployConfig());
        deploy.setDeployDetails(deployConfig);
    }

}
