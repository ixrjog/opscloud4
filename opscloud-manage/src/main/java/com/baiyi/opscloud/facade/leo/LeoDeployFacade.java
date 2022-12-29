package com.baiyi.opscloud.facade.leo;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.domain.param.leo.LeoDeployParam;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildVO;
import com.baiyi.opscloud.domain.vo.leo.LeoDeployVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/12/5 14:31
 * @Version 1.0
 */
public interface LeoDeployFacade {

    void doDeploy(LeoDeployParam.DoDeploy doDeploy);

    List<ApplicationResourceVO.BaseResource> queryLeoBuildDeployment(LeoBuildParam.QueryDeployDeployment queryBuildDeployment);

    List<LeoBuildVO.Build> queryLeoDeployVersion(LeoBuildParam.QueryDeployVersion queryBuildVersion);

    DataTable<LeoDeployVO.Deploy> queryLeoJobDeployPage(LeoJobParam.JobDeployPageQuery pageQuery);

}
