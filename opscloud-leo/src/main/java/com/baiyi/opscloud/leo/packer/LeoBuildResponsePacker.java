package com.baiyi.opscloud.leo.packer;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.leo.domain.model.LeoBuildModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/11/24 17:46
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LeoBuildResponsePacker {

    //  @EnvWrapper(extend = true)
    @AgoWrapper(extend = true)
    public void wrap(LeoBuildVO.Build build) {
        LeoBuildModel.BuildConfig buildConfig = LeoBuildModel.load(build.getBuildConfig());
        build.setBuildDetails(buildConfig);
    }

}
