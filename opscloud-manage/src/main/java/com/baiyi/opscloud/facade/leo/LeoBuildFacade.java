package com.baiyi.opscloud.facade.leo;

import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;

/**
 * @Author baiyi
 * @Date 2022/11/8 16:15
 * @Version 1.0
 */
public interface LeoBuildFacade {

    void doBuild(LeoBuildParam.DoBuild doBuild);

    LeoBuildVO.BranchOptions getBuildBranchOptions(LeoBuildParam.GetBuildBranchOptions getOptions);

}
