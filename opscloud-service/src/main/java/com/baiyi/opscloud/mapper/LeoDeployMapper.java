package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface LeoDeployMapper extends Mapper<LeoDeploy> {

    Integer getMaxDeployNumberWithJobId(Integer jobId);

    List<ReportVO.Report> statByMonth();

    /**
     * 统计最近30天生产环境变更
     *
     * @return
     */
    List<ReportVO.Report> statLast30Days();

    List<ReportVO.Report> statByEnvName();

    Integer countByEnvProjectId(Integer projectId, Integer envType);

}