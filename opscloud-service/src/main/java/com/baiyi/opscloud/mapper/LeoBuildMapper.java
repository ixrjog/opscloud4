package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface LeoBuildMapper extends Mapper<LeoBuild> {

    Integer getMaxBuildNumberWithJobId(Integer jobId);

    List<LeoBuild> queryPageByParam(LeoJobParam.JobBuildPageQuery pageQuery);

    List<ReportVO.Report> statByMonth();

    List<ReportVO.Report> queryMonth();

    int statUserTotal();

    List<ReportVO.Report> statByEnvName();

}