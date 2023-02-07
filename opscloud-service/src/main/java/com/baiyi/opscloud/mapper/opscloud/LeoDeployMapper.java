package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface LeoDeployMapper extends Mapper<LeoDeploy> {

    Integer getMaxDeployNumberWithJobId(Integer jobId);

    List<ReportVO.Report> statByMonth();

    List<ReportVO.Report> statByEnvName();

}