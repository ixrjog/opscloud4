package com.baiyi.opscloud.facade.leo;

import com.baiyi.opscloud.domain.param.leo.LeoBuildPipelineParam;
import com.baiyi.opscloud.domain.vo.leo.LeoBuildPipelineVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/11/28 16:39
 * @Version 1.0
 */
public interface LeoBuildPipelineFacade {

    List<LeoBuildPipelineVO.Step> getPipelineRunNodeSteps(LeoBuildPipelineParam.GetPipelineRunNodeSteps param);

}
